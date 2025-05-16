package com.example.airline.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.airline.dto.TicketRequest;
import com.example.airline.exception.ResourceNotFoundException;
import com.example.airline.exception.ValidationException;
import com.example.airline.model.Schedule;
import com.example.airline.model.Ticket;
import com.example.airline.repository.ScheduleRepository;
import com.example.airline.repository.TicketRepository;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private TicketService ticketService;

    private Schedule schedule;

    @BeforeEach
    public void setUp() {
        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setAvailableSeats(10);
    }

    // createTicket tests
    @Test
    public void testCreateTicket_Success() {
        TicketRequest request = new TicketRequest();
        request.setScheduleId(1L);
        request.setPassengerName("John Doe");
        request.setPassengerEmail("john.doe@example.com");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

        Ticket ticket = ticketService.createTicket(request);

        assertNotNull(ticket);
        assertEquals("BOOKED", ticket.getStatus());
        assertEquals("John Doe", ticket.getPassengerName());
        assertEquals("john.doe@example.com", ticket.getPassengerEmail());
        assertEquals(9, schedule.getAvailableSeats()); // 1 seat deducted
    }

    @Test
    public void testCreateTicket_NoSeatsAvailable() {
        schedule.setAvailableSeats(0);
        TicketRequest request = new TicketRequest();
        request.setScheduleId(1L);
        request.setPassengerName("John");
        request.setPassengerEmail("john@example.com");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        assertThrows(ValidationException.class, () -> ticketService.createTicket(request));
    }

    @Test
    public void testCreateTicket_InvalidEmail() {
        TicketRequest request = new TicketRequest();
        request.setScheduleId(1L);
        request.setPassengerName("John");
        request.setPassengerEmail("invalid");

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));

        assertThrows(ValidationException.class, () -> ticketService.createTicket(request));
    }

    @Test
    public void testCreateTicket_ScheduleNotFound() {
        TicketRequest request = new TicketRequest();
        request.setScheduleId(99L);
        request.setPassengerName("John");
        request.setPassengerEmail("john@example.com");

        when(scheduleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.createTicket(request));
    }

    // getTicket tests
    @Test
    public void testGetTicket_Success() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.getTicket(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetTicket_NotFound() {
        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ticketService.getTicket(2L));
    }

    // cancelTicket tests
    @Test
    public void testCancelTicket_Success() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus("BOOKED");
        ticket.setSchedule(schedule);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any())).thenReturn(ticket);
        when(scheduleRepository.save(any())).thenReturn(schedule);

        ticketService.cancelTicket(1L);

        assertEquals("CANCELLED", ticket.getStatus());
        assertEquals(11, schedule.getAvailableSeats()); // 1 seat added back
    }

    @Test
    public void testCancelTicket_AlreadyCancelled() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setStatus("CANCELLED");
        ticket.setSchedule(schedule);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThrows(ValidationException.class, () -> ticketService.cancelTicket(1L));
    }
}
