package apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle;

import lombok.Data;

@Data
public class VehicleResponseDTO {
    private String id;
    private int rentalVendorId;
    private String type;
    private String brand;
    private String model;
    private int productionYear;
    private String location;
    private String licencePlate;
    private int capacity;
    private String transmission;
    private String fuelType;
    private double price;
    private String status;
}
