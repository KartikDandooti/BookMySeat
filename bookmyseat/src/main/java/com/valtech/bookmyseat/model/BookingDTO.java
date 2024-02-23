package com.valtech.bookmyseat.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDTO {
    private LocalDate bookingStartDate;
    private LocalDate bookingEndDate;
    private int seatId;
    private int floorId;
}
