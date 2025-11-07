package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class UpdateRentalBookingRequestDTO {
    @NotBlank(message = "Vehicle ID tidak boleh kosong")
    private String vehicleId;

    @NotNull(message = "Pick up time tidak boleh kosong")
    @JsonFormat(timezone = "Asia/Jakarta")
    private Date pickUpTime;

    @NotNull(message = "Drop off time tidak boleh kosong")
    @JsonFormat(timezone = "Asia/Jakarta")
    private Date dropOffTime;

    @NotBlank(message = "Pick up location tidak boleh kosong")
    private String pickUpLocation;

    @NotBlank(message = "Drop off location tidak boleh kosong")
    private String dropOffLocation;

    private boolean includeDriver;
   
    @NotBlank(message = "Transmission needed tidak boleh kosong")
    private String transmissionNeeded;

    @Positive(message = "Capacity needed harus lebih dari 0")
    private int capacityNeeded;

    @Positive(message = "Base total price harus lebih dari 0")
    private double baseTotalPrice;
}
