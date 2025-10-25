package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking;

import lombok.Data;
import java.util.Date;

@Data
public class UpdateRentalBookingRequestDTO {
    private String bookingId;
    private String vehicleId;
    private Date pickUpTime;
    private Date dropOffTime;
    private String pickUpLocation;
    private String dropOffLocation;
    private boolean includeDriver;
    private String transmissionNeeded;
    private int capacityNeeded;
    private double baseTotalPrice;
}
