package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking;

import lombok.Data;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UpdateBookingAddOnsRequestDTO {
    @NotBlank(message = "Booking ID tidak boleh kosong")
    private String bookingId;

    @NotNull(message = "AddOns tidak boleh null")
    private List<String> addOns;
}
