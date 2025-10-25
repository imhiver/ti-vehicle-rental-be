package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.CreateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.RentalBookingResponseDTO;

public interface RentalBookingService {
    RentalBookingResponseDTO createBooking(CreateRentalBookingRequestDTO dto) throws Exception;
}
