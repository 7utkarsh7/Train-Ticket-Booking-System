package com.example.train_ticket.controller;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.train_ticket.models.Seat;
import com.example.train_ticket.models.Ticket;
import com.example.train_ticket.models.User;
import com.example.train_ticket.service.TicketService;

@RestController
@RequestMapping("/api")
public class TicketController {
     @Autowired
    private TicketService trainService;

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@RequestBody User user) {
        if(StringUtils.isBlank(user.getEmail())){
            return new ResponseEntity<>("Email is required!", HttpStatus.EXPECTATION_FAILED);
        }
        Ticket ticket = trainService.purchaseTicket(user);
        return ResponseEntity.ok().body(ticket);
    }

    @GetMapping("/receipt")
    public ResponseEntity<?> getReceipt(@RequestParam String email) {
        if(StringUtils.isBlank(email)){
            return new ResponseEntity<>("Email is required!", HttpStatus.EXPECTATION_FAILED);
        }
        Optional<Ticket> ticket = trainService.getReceipt(email);
        return ResponseEntity.ok().body(ticket);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsersBySection(@RequestParam String section) {
        if(StringUtils.isBlank(section)){
            return new ResponseEntity<>("Section is required!", HttpStatus.EXPECTATION_FAILED);
        }
        Map<Integer,User > sectionDetails = trainService.getUsersBySection(section);
        return ResponseEntity.ok().body(sectionDetails);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeUser(@RequestParam String email) {
        if(StringUtils.isBlank(email)){
            return new ResponseEntity<>("Email is required!", HttpStatus.EXPECTATION_FAILED);
        }
        trainService.removeUser(email);
        return ResponseEntity.ok().body("User removed successfully!");
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifySeat(@RequestBody Seat seat,@RequestParam String email) {
        if(StringUtils.isBlank(email)){
            return new ResponseEntity<>("Email is required!", HttpStatus.EXPECTATION_FAILED);
        }
        if(StringUtils.isBlank(seat.getSection()) || Objects.isNull(seat.getSeatNumber()) || seat.getSeatNumber() == 0) {
            return new ResponseEntity<>("Seat Details is required!", HttpStatus.EXPECTATION_FAILED);
        }
        String response = trainService.modifySeat(email, seat);
        return ResponseEntity.ok().body(response);
    }
}
