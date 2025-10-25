package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CreateRentalBookingRequestDTO {
    private String vehicleId;
    private String pickUpLocation;
    private String dropOffLocation;
    private Date pickUpTime;
    private Date dropOffTime;
    private boolean includeDriver;
    private List<String> addOns;
    private int capacityNeeded;
    private String transmissionNeeded;
    private double baseTotalPrice;
}
