package com.example.airline.dto;


import jakarta.validation.constraints.NotNull;// Updated for Spring Boot 3.x
import jakarta.validation.constraints.NotBlank;
public class TicketRequest {
    @NotNull
    private Long scheduleId;

    @NotBlank
    private String passengerName;

    @NotBlank
    private String passengerEmail;

    // Getters and setters
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public String getPassengerEmail() { return passengerEmail; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }
}