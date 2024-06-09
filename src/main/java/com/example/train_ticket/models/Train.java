package com.example.train_ticket.models;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Train {
private Map<String, Ticket> sectionA;
    private Map<String, Ticket> sectionB;
    private Integer sectionTotal = 40;

    public Train() {
        this.sectionA = new HashMap<>();
        this.sectionB = new HashMap<>();
    }
}
