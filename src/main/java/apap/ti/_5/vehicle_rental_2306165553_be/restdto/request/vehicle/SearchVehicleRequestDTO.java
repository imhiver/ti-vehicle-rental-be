package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle;

import lombok.Data;
import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class SearchVehicleRequestDTO {
    @NotBlank(message = "Pick up location tidak boleh kosong")
    private String pickUpLocation;

    @NotBlank(message = "Drop off location tidak boleh kosong")
    private String dropOffLocation;

    @NotNull(message = "Pick up time tidak boleh kosong")
    private Date pickUpTime;

    @NotNull(message = "Drop off time tidak boleh kosong")
    private Date dropOffTime;

    @Positive(message = "Capacity needed harus lebih dari 0")
    private int capacityNeeded;

    @NotBlank(message = "Transmission needed tidak boleh kosong")
    private String transmissionNeeded;

    private boolean includeDriver;
}
