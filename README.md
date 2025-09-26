# Book My Seat Web Platform Documentation

## Overview
"BookMySeat" is a web application designed to streamline the process of seat reservation for employees in workspace. With the evolving dynamics of flexible work arrangements and the need for social distancing, providing employees with a tool to reserve their seats in advance ensures a smooth and organized transition into the office environment.

## Table of Contents
- [Objective](#objective)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Installation](#installation)

## Objective
The primary objective of BookMySeat is to facilitate efficient seat booking for employees, allowing them to plan their office visits in advance and ensuring adherence to office capacity and social distancing guidelines. By enabling employees to reserve their seats remotely, the application aims to enhance workplace flexibility and safety.

## Features
- ### User Authentication:
 Employees will have individual accounts secured with authentication mechanisms to access the application.

- ### Seat Reservation: 
The core feature of the application, allowing users to view the available seats within the office premises and reserve a seat for a specified date and time.

- ### Seat Availability:
 Real-time updates on seat availability, displaying booked and unbooked seats to users to make informed decisions.

- ### Personalized Dashboard: 
Each user will have a personalized dashboard displaying their upcoming seat reservations, history, and preferences.

- ### Notifications: 
Automated notifications to remind users of their upcoming reservations and any changes in seat availability.

- ### Admin Panel: 
An administrative interface to manage seat allocations, office layouts, user accounts, and generate reports on seat occupancy.

- ### Office Layout Visualization: 
Interactive visualization of the office layout, allowing users to select seats based on their preferences and proximity to amenities.

- ### Booking Policies: 
Implementation of policies regarding booking limits, cancellation rules, and prioritization for certain employee groups (e.g., priority seating for executives).

- ### Integration: 
Seamless integration with existing office management systems and calendar applications to synchronize seat reservations with employees' schedules.

## Technology Stack
- Database: MySQL
- Frontend: React
- Backend: MySQL/SQL JDBC Template, Spring Boot

## Installation
1. Clone the repository: git clone https://github.com/Kruthik71/BookMySeat.git
2. Set up the MySQL database and configure the connection in the application.
3. Navigate to the project directory and run the application: mvn clean spring-boot:run
4. Access the application at Backend-[http://localhost:9006](http://localhost:9006), Frontend-[http://localhost:3000](http://localhost:3000)
