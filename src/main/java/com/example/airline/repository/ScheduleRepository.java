package com.example.airline.repository;

import com.example.airline.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByFlightIdAndDepartureTimeAfter(Long flightId, LocalDateTime date);
}