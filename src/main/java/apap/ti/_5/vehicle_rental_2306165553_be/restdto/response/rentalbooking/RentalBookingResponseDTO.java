package apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class RentalBookingResponseDTO {
    private String id;
    private String vehicleId;
    @JsonFormat(timezone = "Asia/Jakarta")
    private Date pickUpTime;
    @JsonFormat(timezone = "Asia/Jakarta")
    private Date dropOffTime;
    private String pickUpLocation;
    private String dropOffLocation;
    private String status;
    private double totalPrice;
    private List<String> addOnNames;
    private boolean includeDriver;
    private String transmissionNeeded;
    private int capacityNeeded;
}
