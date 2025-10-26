package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CreateRentalBookingRequestDTO {
    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    @NotBlank(message = "Pick up location is required")
    private String pickUpLocation;

    @NotBlank(message = "Drop off location is required")
    private String dropOffLocation;

    @NotNull(message = "Pick up time is required")
    private Date pickUpTime;

    @NotNull(message = "Drop off time is required")
    private Date dropOffTime;

    @NotNull(message = "Include driver field is required")
    private boolean includeDriver;

    @Size(max = 5, message = "Maximum 5 add-ons allowed")
    private List<@NotBlank(message = "Add-on cannot be blank") String> addOns;

    @Min(value = 1, message = "Capacity needed must be at least 1")
    private int capacityNeeded;

    @NotBlank(message = "Transmission needed is required")
    private String transmissionNeeded;

    @NotNull(message = "Base total price is required")
    @Min(value = 1, message = "Base total price must be greater than 0")
    private Double baseTotalPrice;
}
