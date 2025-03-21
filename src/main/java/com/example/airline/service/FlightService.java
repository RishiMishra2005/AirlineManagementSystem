package com.example.airline.service;

import com.example.airline.exception.ResourceNotFoundException;
import com.example.airline.model.Flight;
import com.example.airline.model.Schedule;
import com.example.airline.repository.FlightRepository;
import com.example.airline.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Flight> getAllFlights(String sort) {
        if ("asc".equalsIgnoreCase(sort)) {
            return flightRepository.findAllByOrderByFlightNumberAsc();
        }
        return flightRepository.findAll();
    }

    public Flight getFlight(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
    }

    public List<Schedule> getFlightSchedules(Long flightId, String dates) {
        LocalDateTime dateFilter = dates != null ?
                LocalDateTime.parse(dates) : LocalDateTime.now();
        return scheduleRepository.findByFlightIdAndDepartureTimeAfter(flightId, dateFilter);
    }
}