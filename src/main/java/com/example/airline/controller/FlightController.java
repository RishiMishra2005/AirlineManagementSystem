package com.example.airline.controller;

import com.example.airline.model.Flight;
import com.example.airline.model.Schedule;
import com.example.airline.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights(
            @RequestParam(required = false) String sort) {
        return ResponseEntity.ok(flightService.getAllFlights(sort));
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlight(id));
    }

    @GetMapping("/flights/{id}/schedules")
    public ResponseEntity<List<Schedule>> getFlightSchedules(
            @PathVariable Long id,
            @RequestParam(required = false) String dates) {
        return ResponseEntity.ok(flightService.getFlightSchedules(id, dates));
    }
}