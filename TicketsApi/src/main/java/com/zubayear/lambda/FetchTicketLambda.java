package com.zubayear.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.zubayear.models.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FetchTicketLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Logger logger = LogManager.getLogger(FetchTicketLambda.class);

    private final ObjectMapper mapper = new ObjectMapper();
    private final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        logger.info("Received request: {}", input.getBody());

        ScanResult ticketsTable = amazonDynamoDB.scan(new ScanRequest().withTableName(System.getenv("TICKETS_TABLE")));

        List<Ticket> ticketsFromRepo = ticketsTable.getItems()
                .stream()
                .map(item -> new Ticket(
                        UUID.fromString(item.get("id").getS()),
                        LocalDate.parse(item.get("matchTime").getS()),
                        item.get("stadium").getS(),
                        item.get("ticketName").getS(),
                        Double.parseDouble(item.get("ticketPrice").getN())
                )).collect(Collectors.toList());
        String response;

        mapper.registerModule(new JodaModule());
        try {
            response = mapper.writeValueAsString(ticketsFromRepo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(response);
    }
}
