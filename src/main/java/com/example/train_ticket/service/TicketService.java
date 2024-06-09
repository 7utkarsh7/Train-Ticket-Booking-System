package com.example.train_ticket.service;

import com.example.train_ticket.models.Ticket;
import com.example.train_ticket.models.Train;
import com.example.train_ticket.models.User;
import com.example.train_ticket.models.Seat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private Train train;

    public TicketService() {
        this.train = new Train();
    }

    public Ticket purchaseTicket(User user) {
        Seat seat = allocateSeat(user);
        Ticket ticket = new Ticket("London", "France", user, 20.0, seat);
        getSectionBySeat(seat).put(user.getEmail(), ticket);
        return ticket;
    }

    public Optional<Ticket> getReceipt(String email) {
        return Optional.ofNullable(findTicketByEmail(email));
    }

    public Map<Integer, User> getUsersBySection(String section) {
        Map<String, Ticket> sectionDetails = "A".equalsIgnoreCase(section) ? train.getSectionA() : train.getSectionB();
        Map<Integer,User > usersBySection = new HashMap<>();
        for (Map.Entry<String, Ticket> entry : sectionDetails.entrySet()) {
            Ticket ticket = entry.getValue();
            User user = ticket.getUser(); 

            usersBySection.put(ticket.getSeat().getSeatNumber(),user);
        }

        return usersBySection;
    }

    public void removeUser(String email) {
        Ticket ticket = findTicketByEmail(email);
        if (ticket != null) {
            getSectionBySeat(ticket.getSeat()).remove(email);
            reallocateSeats(ticket.getSeat());
        }
    }

    public void reallocateSeats(Seat seat) {
        Map<String, Ticket> sectionDetails = getSectionBySeat(seat);
        for (Map.Entry<String, Ticket> entry : sectionDetails.entrySet()) {
            Ticket currentTicket = entry.getValue();
            Integer currentSeatNumber = currentTicket.getSeat().getSeatNumber();
            if(currentSeatNumber > seat.getSeatNumber()){
                currentTicket.getSeat().setSeatNumber(currentSeatNumber-1);
            }
        }
    }

    public String modifySeat(String email, Seat newSeat) {
        Ticket ticket = findTicketByEmail(email);
        if (ticket != null && seatAvailable(email,newSeat)) {
            removeUser(email);
            ticket.setSeat(newSeat);
            getSectionBySeat(newSeat).put(email, ticket);
            return "Modified";
        }else{
            return "Seat not available or no current ticket exists!";
        }
    }
    Boolean seatAvailable(String email, Seat newSeat){
        Map<String, Ticket> sectionDetails = getSectionBySeat(newSeat);
        for (Map.Entry<String, Ticket> entry : sectionDetails.entrySet()) {
            Ticket currentTicket = entry.getValue();
            if(currentTicket.getSeat().getSeatNumber() == newSeat.getSeatNumber()){
                return false;
            }
        }
        return true;
    }

    private Seat allocateSeat(User user) {
        int aSize = train.getSectionA().size();
        int bSize = train.getSectionB().size();
        String section = aSize <= bSize ? "A" : "B";
        Integer seatNumber = getSeatNumber(section);
        return new Seat(section,seatNumber);
    }

    private Integer getSeatNumber(String section) {
        if(section.equalsIgnoreCase("A")){
            Integer aSize = train.getSectionA().size();
            return aSize+1;
        }else{
            int bSize = train.getSectionB().size();
            return bSize+1;
        }
    }

    private Map<String, Ticket> getSectionBySeat(Seat seat) {
        return "A".equalsIgnoreCase(seat.getSection()) ? train.getSectionA() : train.getSectionB();
    }

    private Ticket findTicketByEmail(String email) {
        Ticket ticket = train.getSectionA().get(email);
        return ticket != null ? ticket : train.getSectionB().get(email);
    }
}
