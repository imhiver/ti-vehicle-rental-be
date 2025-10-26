package apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateBookingStatusRequestDTO {
    @NotBlank(message = "Booking ID tidak boleh kosong")
    private String bookingId;

    @NotBlank(message = "Status baru tidak boleh kosong")
    private String newStatus;
}
