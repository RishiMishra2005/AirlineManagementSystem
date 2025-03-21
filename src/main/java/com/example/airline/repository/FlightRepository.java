package com.example.airline.repository;

import com.example.airline.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findAllByOrderByFlightNumberAsc();
}