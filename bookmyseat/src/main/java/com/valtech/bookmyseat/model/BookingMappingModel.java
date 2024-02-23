package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingMappingModel {
    private int id;
    private LocalDate bookedDate;
    private boolean attendance;
    private int bookingId; 
}