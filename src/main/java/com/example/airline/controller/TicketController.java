package com.example.airline.controller;

import com.example.airline.dto.TicketRequest;
import com.example.airline.model.Ticket;
import com.example.airline.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; // Updated from javax to jakarta

@RestController
@RequestMapping("/api")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/tickets")
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.createTicket(ticketRequest));
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }
}