package com.example.airline.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.airline.exception.ResourceNotFoundException;
import com.example.airline.model.Flight;
import com.example.airline.model.Schedule;
import com.example.airline.repository.FlightRepository;
import com.example.airline.repository.ScheduleRepository;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getAllFlights()
    @Test
    public void testGetAllFlights_WithAscSort() {
        Flight f1 = new Flight(); f1.setFlightNumber("AA100");
        Flight f2 = new Flight(); f2.setFlightNumber("BA200");

        when(flightRepository.findAllByOrderByFlightNumberAsc())
                .thenReturn(Arrays.asList(f1, f2));

        List<Flight> result = flightService.getAllFlights("asc");

        assertEquals(2, result.size());
        verify(flightRepository).findAllByOrderByFlightNumberAsc();
    }

    @Test
    public void testGetAllFlights_WithoutSort() {
        Flight f1 = new Flight(); f1.setFlightNumber("CA300");

        when(flightRepository.findAll())
                .thenReturn(Collections.singletonList(f1));

        List<Flight> result = flightService.getAllFlights(null);

        assertEquals(1, result.size());
        verify(flightRepository).findAll();
    }

    // Test for getFlight()
    @Test
    public void testGetFlight_Exists() {
        Flight flight = new Flight(); flight.setId(1L);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Flight result = flightService.getFlight(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetFlight_NotFound() {
        when(flightRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> flightService.getFlight(2L));
    }

    // Test for getFlightSchedules()
    @Test
    public void testGetFlightSchedules_WithDateFilter() {
        Long flightId = 1L;
        String dateStr = "2025-05-10T10:00:00";
        LocalDateTime dateTime = LocalDateTime.parse(dateStr);

        Schedule s1 = new Schedule(); s1.setDepartureTime(dateTime.plusHours(1));

        when(scheduleRepository.findByFlightIdAndDepartureTimeAfter(flightId, dateTime))
                .thenReturn(Collections.singletonList(s1));

        List<Schedule> result = flightService.getFlightSchedules(flightId, dateStr);

        assertEquals(1, result.size());
        verify(scheduleRepository)
            .findByFlightIdAndDepartureTimeAfter(flightId, dateTime);
    }

    @Test
    public void testGetFlightSchedules_WithNullDate() {
        Long flightId = 1L;

        Schedule s1 = new Schedule(); s1.setDepartureTime(LocalDateTime.now().plusDays(1));

        when(scheduleRepository.findByFlightIdAndDepartureTimeAfter(eq(flightId), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(s1));

        List<Schedule> result = flightService.getFlightSchedules(flightId, null);

        assertEquals(1, result.size());
        verify(scheduleRepository)
            .findByFlightIdAndDepartureTimeAfter(eq(flightId), any(LocalDateTime.class));
    }
}
