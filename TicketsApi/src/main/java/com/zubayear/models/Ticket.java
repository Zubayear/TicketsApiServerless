package com.zubayear.models;

import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Ticket {
    private UUID id;
    private LocalDate matchTime;
    private String stadium;
    private String ticketName;
    private double ticketPrice;

    public Ticket() {
    }

    public Ticket(UUID id, LocalDate matchTime, String stadium, String ticketName, double ticketPrice) {
        this.id = id;
        this.matchTime = matchTime;
        this.stadium = stadium;
        this.ticketName = ticketName;
        this.ticketPrice = ticketPrice;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(LocalDate matchTime) {
        this.matchTime = matchTime;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(matchTime, ticket.matchTime) && Objects.equals(stadium, ticket.stadium) && Objects.equals(ticketName, ticket.ticketName) && Objects.equals(ticketPrice, ticket.ticketPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matchTime, stadium, ticketName, ticketPrice);
    }

    @Override
    public String toString() {
        return String.format("{\"id\": \"%s\",\"matchTime\": \"%s\", \"ticketName\": \"%s\",\"ticketPrice\": \"%s\",\"stadium\": \"%s\"}", id, matchTime, ticketName, ticketPrice, stadium);
    }
}
