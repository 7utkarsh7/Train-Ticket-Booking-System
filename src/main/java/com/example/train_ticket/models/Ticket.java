package com.example.train_ticket.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private String from;
    private String to;
    private User user;
    private double price;
    private Seat seat;
}
