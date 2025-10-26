package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.CreateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingAddOnsRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingStatusRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.BookingChartResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.RentalBookingResponseDTO;
import java.util.List;

public interface RentalBookingService {
    List<RentalBookingResponseDTO> getAllBookings(String search);
    RentalBookingResponseDTO createBooking(CreateRentalBookingRequestDTO dto) throws Exception;
    RentalBookingResponseDTO getBookingById(String id) throws Exception;
    RentalBookingResponseDTO updateBooking(String id, UpdateRentalBookingRequestDTO dto) throws Exception;
    RentalBookingResponseDTO updateBookingStatus(UpdateBookingStatusRequestDTO dto) throws Exception;
    RentalBookingResponseDTO updateBookingAddOns(UpdateBookingAddOnsRequestDTO dto) throws Exception;
    void cancelBooking(String id) throws Exception;
    List<BookingChartResponseDTO> getBookingChart(String period, int year);
}
