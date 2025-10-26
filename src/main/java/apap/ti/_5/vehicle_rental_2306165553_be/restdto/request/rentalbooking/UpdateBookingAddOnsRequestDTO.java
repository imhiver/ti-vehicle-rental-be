package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking;

import lombok.Data;
import java.util.List;

@Data
public class UpdateBookingAddOnsRequestDTO {
    private String bookingId;
    private List<String> addOns;
}
