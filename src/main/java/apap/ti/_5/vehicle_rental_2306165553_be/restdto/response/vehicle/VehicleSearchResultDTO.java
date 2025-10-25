package apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle;

import lombok.Data;

@Data
public class VehicleSearchResultDTO {
    private String id;
    private String type;
    private String brand;
    private String model;
    private String rentalVendorName;
    private double totalPrice;
}
