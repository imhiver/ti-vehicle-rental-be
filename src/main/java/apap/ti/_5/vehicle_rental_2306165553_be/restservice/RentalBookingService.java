package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import java.util.List;

import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.CreateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.RentalBookingResponseDTO;

public interface RentalBookingService {
    List<RentalBookingResponseDTO> getAllBookings(String search);
    RentalBookingResponseDTO createBooking(CreateRentalBookingRequestDTO dto) throws Exception;
}
