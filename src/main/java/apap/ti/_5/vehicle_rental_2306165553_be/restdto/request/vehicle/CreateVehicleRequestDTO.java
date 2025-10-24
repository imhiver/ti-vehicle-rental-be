package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateVehicleRequestDTO {
    @NotNull(message = "Rental vendor ID is required")
    private int rentalVendorId;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Production year is required")
    @Min(value = 1900, message = "Production year must be valid")
    private Integer productionYear;

    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @NotBlank(message = "Licence plate is required")
    @Size(max = 20, message = "Licence plate must not exceed 20 characters")
    private String licencePlate;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    @NotBlank(message = "Transmission is required")
    private String transmission;

    @NotBlank(message = "Fuel type is required")
    private String fuelType;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
}
