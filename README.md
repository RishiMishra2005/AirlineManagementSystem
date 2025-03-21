Airline Management System
Overview
The Airline Management System is a RESTful API built with Spring Boot that allows users to manage flight schedules and tickets. It provides endpoints to retrieve flight information, book tickets, view ticket details, and cancel bookings, with validations to ensure data integrity. This project serves as a backend service for an airline management application, ideal for educational purposes or as a foundation for a real-world system.

Features
Flight Management:
Retrieve all flights with optional sorting by flight number (GET /api/flights?sort=asc).
Get details of a specific flight (GET /api/flights/{id}).
Fetch schedules for a flight, filtered by date (GET /api/flights/{id}/schedules?dates=).
Ticket Management:
Book a ticket for a flight schedule (POST /api/tickets).
Retrieve ticket details (GET /api/tickets/{id}).
Cancel a ticket (DELETE /api/tickets/{id}).
Validations:
Prevents booking when no seats are available.
Ensures valid email format for passengers.
Blocks duplicate ticket cancellations.
Tech Stack
Framework: Spring Boot 3.1.0
Language: Java 17
Database: H2 (in-memory)
Dependencies: Spring Web, Spring Data JPA, H2 Database, Spring Validation
Build Tool: Maven
Getting Started
Prerequisites
Java 17 or higher
Maven 3.6+
Git (to clone the repository)
A tool like Postman or curl for testing API endpoints
Installation
Clone the Repository:
bash
git clone https://github.com/your-username/airline-management-system.git
cd airline-management-system

Build the Project:
bash
mvn clean install

Run the Application:
bash
mvn spring-boot:run
The app will start on http://localhost:8080.
Initial Setup
Since this uses an H2 in-memory database, you need to populate initial data:

Open the H2 console at http://localhost:8080/h2-console.
Log in with:
JDBC URL: jdbc:h2:mem:airline_db
Username: sa
Password: (leave blank)

Run this SQL to add sample data:
sql
INSERT INTO FLIGHT (FLIGHT_NUMBER, ORIGIN, DESTINATION) 
VALUES ('AA101', 'New York', 'Los Angeles');

INSERT INTO SCHEDULE (FLIGHT_ID, DEPARTURE_TIME, ARRIVAL_TIME, AVAILABLE_SEATS, PRICE) 
VALUES (1, '2025-03-18 10:00:00', '2025-03-18 13:00:00', 50, 199.99);
API Endpoints
Flights
Get All Flights:
GET /api/flights?sort=asc
Returns all flights, sorted by flight number if sort=asc.
Get a Flight:
GET /api/flights/{id}
Example: GET /api/flights/1
Get Flight Schedules:
GET /api/flights/{id}/schedules?dates={date}
Example: GET /api/flights/1/schedules?dates=2025-03-17T00:00:00
Tickets
Book a Ticket:
POST /api/tickets

Body:
json
{
    "scheduleId": 1,
    "passengerName": "John Doe",
    "passengerEmail": "john.doe@example.com"
}
Response: 201 Created with ticket details.
Get Ticket Details:
GET /api/tickets/{id}
Example: GET /api/tickets/1
Cancel a Ticket:
DELETE /api/tickets/{id}
Example: DELETE /api/tickets/1
Response: 204 No Content
Usage Examples
Using curl

Get Flights:
bash
curl http://localhost:8080/api/flights?sort=asc

Book a Ticket:
curl -X POST -H "Content-Type: application/json" -d '{"scheduleId":1,"passengerName":"Jane Doe","passengerEmail":"jane@example.com"}' http://localhost:8080/api/tickets

curl -X DELETE http://localhost:8080/api/tickets/1
Using Postman
Create a new request.
Set the method (GET/POST/DELETE) and URL (e.g., http://localhost:8080/api/flights).
For POST, add the JSON body under the "Body" tab (raw, JSON format).
Send the request and view the response.

Images ->

Booking a ticket:

![Screenshot 2025-03-19 192651](https://github.com/user-attachments/assets/daf1bb71-0ebc-4f63-8afc-436d6518d984)

Cancelling a ticket:

![Screenshot 2025-03-19 192554](https://github.com/user-attachments/assets/9f8f813b-e177-4f40-92e0-c59198794cf5)
![Screenshot 2025-03-19 192605](https://github.com/user-attachments/assets/0b5e3ce6-457a-4632-9652-be912d2bc86a)

Get ticket details:

![Screenshot 2025-03-19 192408](https://github.com/user-attachments/assets/4c4a3c37-c544-4a71-a801-f0edcaf95b0b)

Get flight schedule:

![Screenshot 2025-03-19 192355](https://github.com/user-attachments/assets/fcf77bd2-3967-45a8-9304-b7e944ed8277)
![Screenshot 2025-03-19 192130](https://github.com/user-attachments/assets/ca342c39-c9ea-4a64-aedd-2a156da06224)
