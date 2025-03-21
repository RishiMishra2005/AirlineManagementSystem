package com.example.airline.service;

import com.example.airline.dto.TicketRequest;
import com.example.airline.exception.ResourceNotFoundException;
import com.example.airline.exception.ValidationException;
import com.example.airline.model.Schedule;
import com.example.airline.model.Ticket;
import com.example.airline.repository.ScheduleRepository;
import com.example.airline.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import jakarta.validation.constraints.NotNull; // Updated from javax to jakarta
import jakarta.validation.constraints.NotBlank;
@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public Ticket createTicket(TicketRequest ticketRequest) {
        Schedule schedule = scheduleRepository.findById(ticketRequest.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        if (schedule.getAvailableSeats() <= 0) {
            throw new ValidationException("No seats available");
        }

        if (!isValidEmail(ticketRequest.getPassengerEmail())) {
            throw new ValidationException("Invalid email format");
        }

        Ticket ticket = new Ticket();
        ticket.setSchedule(schedule);
        ticket.setPassengerName(ticketRequest.getPassengerName());
        ticket.setPassengerEmail(ticketRequest.getPassengerEmail());
        ticket.setStatus("BOOKED");
        ticket.setBookingTime(LocalDateTime.now());

        schedule.setAvailableSeats(schedule.getAvailableSeats() - 1);
        scheduleRepository.save(schedule);

        return ticketRepository.save(ticket);
    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    }

    public void cancelTicket(Long id) {
        Ticket ticket = getTicket(id);
        if ("CANCELLED".equals(ticket.getStatus())) {
            throw new ValidationException("Ticket already cancelled");
        }

        ticket.setStatus("CANCELLED");
        Schedule schedule = ticket.getSchedule();
        schedule.setAvailableSeats(schedule.getAvailableSeats() + 1);

        ticketRepository.save(ticket);
        scheduleRepository.save(schedule);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}