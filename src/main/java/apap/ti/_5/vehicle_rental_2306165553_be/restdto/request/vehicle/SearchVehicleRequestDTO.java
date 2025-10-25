package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle;

import lombok.Data;
import java.util.Date;

@Data
public class SearchVehicleRequestDTO {
    private String pickUpLocation;
    private String dropOffLocation;
    private Date pickUpTime;
    private Date dropOffTime;
    private int capacityNeeded;
    private String transmissionNeeded;
    private boolean includeDriver;
}
