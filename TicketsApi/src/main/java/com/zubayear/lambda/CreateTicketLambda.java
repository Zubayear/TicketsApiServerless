package com.zubayear.lambda;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.zubayear.models.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateTicketLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    static final Logger logger = LogManager.getLogger(CreateTicketLambda.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        logger.info("Received request: {}", input.getBody());

        // mapping

        mapper.registerModule(new JodaModule());
        Ticket ticket = new Ticket();


        DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
        Table ticketsTable = dynamoDB.getTable(System.getenv("TICKETS_TABLE"));

        try {
            ticket = mapper.readValue(input.getBody(), Ticket.class);
            Item ticketToInsert = new Item()
                    .withPrimaryKey("id", ticket.getId().toString())
                    .withString("matchTime", ticket.getMatchTime().toString())
                    .withString("ticketName", ticket.getTicketName())
                    .withDouble("ticketPrice", ticket.getTicketPrice())
                    .withString("stadium", ticket.getStadium());
            ticketsTable.putItem(ticketToInsert);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(201)
                .withBody(ticket.toString());
    }
}
