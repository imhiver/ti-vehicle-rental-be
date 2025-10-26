package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.CreateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingStatusRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingAddOnsRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.BaseResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.RentalBookingResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.BookingChartResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restservice.RentalBookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class RentalBookingController {
    @Autowired
    private RentalBookingService rentalBookingService;

    public static final String VIEW_BOOKING_DETAIL =  "/{id}";
    public static final String CREATE_BOOKING =  "/create";
    public static final String UPDATE_BOOKING =  "/{id}/update";
    public static final String DELETE_BOOKING =  "/{id}/delete";
    public static final String SEARCH_BOOKING =  "/search";
    public static final String UPDATE_BOOKING_STATUS = "/update-status";
    public static final String UPDATE_BOOKING_ADDONS = "/update-addons";


    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<List<RentalBookingResponseDTO>>> getAllBookings(
        @RequestParam(required = false) String search
    ) {
        var baseResponseDTO = new BaseResponseDTO<List<RentalBookingResponseDTO>>();

        List<RentalBookingResponseDTO> bookings = rentalBookingService.getAllBookings(search);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("Bookings retrieved successfully");
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(bookings);
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping(VIEW_BOOKING_DETAIL)
    public ResponseEntity<BaseResponseDTO<RentalBookingResponseDTO>> getBookingById(@PathVariable("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<RentalBookingResponseDTO>();

        RentalBookingResponseDTO booking;
        try {
            booking = rentalBookingService.getBookingById(id);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Booking not found: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("Booking retrieved successfully");
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(booking);
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PostMapping(CREATE_BOOKING)
    public ResponseEntity<BaseResponseDTO<RentalBookingResponseDTO>> createBooking(
            @Valid @RequestBody CreateRentalBookingRequestDTO dto,
            BindingResult bindingResult) {

        var baseResponse = new BaseResponseDTO<RentalBookingResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            StringBuilder errs = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errs.append(fe.getDefaultMessage()).append("; ");
            }
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage(errs.toString());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        RentalBookingResponseDTO booking;
        try {
            booking = rentalBookingService.createBooking(dto);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Failed to create booking: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setMessage("Booking created successfully");
        baseResponse.setTimestamp(new Date());
        baseResponse.setData(booking);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping(UPDATE_BOOKING)
    public ResponseEntity<BaseResponseDTO<RentalBookingResponseDTO>> updateBooking(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateRentalBookingRequestDTO dto,
            BindingResult bindingResult) {

        var baseResponse = new BaseResponseDTO<RentalBookingResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            StringBuilder errs = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errs.append(fe.getDefaultMessage()).append("; ");
            }
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage(errs.toString());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        RentalBookingResponseDTO booking;
        try {
            booking = rentalBookingService.updateBooking(id, dto);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Failed to update booking: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setMessage("Booking updated successfully");
        baseResponse.setTimestamp(new Date());
        baseResponse.setData(booking);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping(UPDATE_BOOKING_STATUS)
    public ResponseEntity<BaseResponseDTO<RentalBookingResponseDTO>> updateBookingStatus(
            @Valid @RequestBody UpdateBookingStatusRequestDTO dto,
            BindingResult bindingResult) {

        var baseResponse = new BaseResponseDTO<RentalBookingResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            StringBuilder errs = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errs.append(fe.getDefaultMessage()).append("; ");
            }
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage(errs.toString());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        RentalBookingResponseDTO booking;
        try {
            booking = rentalBookingService.updateBookingStatus(dto);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Failed to update booking status: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setMessage("Booking status updated successfully");
        baseResponse.setTimestamp(new Date());
        baseResponse.setData(booking);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PutMapping(UPDATE_BOOKING_ADDONS)
    public ResponseEntity<BaseResponseDTO<RentalBookingResponseDTO>> updateBookingAddOns(
            @Valid @RequestBody UpdateBookingAddOnsRequestDTO dto,
            BindingResult bindingResult) {

        var baseResponse = new BaseResponseDTO<RentalBookingResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            StringBuilder errs = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errs.append(fe.getDefaultMessage()).append("; ");
            }
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage(errs.toString());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }

        RentalBookingResponseDTO booking;
        try {
            booking = rentalBookingService.updateBookingAddOns(dto);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponse.setMessage("Failed to update booking add-ons: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setMessage("Booking add-ons updated successfully");
        baseResponse.setTimestamp(new Date());
        baseResponse.setData(booking);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @DeleteMapping(DELETE_BOOKING)
    public ResponseEntity<BaseResponseDTO<Void>> deleteBooking(@PathVariable("id") String id) {
        var baseResponse = new BaseResponseDTO<Void>();
        try {
            rentalBookingService.cancelBooking(id);
            baseResponse.setStatus(HttpStatus.OK.value());
            baseResponse.setMessage("Booking canceled successfully");
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponse.setMessage("Failed to cancel booking: " + e.getMessage());
            baseResponse.setTimestamp(new Date());
            baseResponse.setData(null);
            return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/chart")
    public ResponseEntity<BaseResponseDTO<List<BookingChartResponseDTO>>> getBookingChart(
        @RequestParam String period,
        @RequestParam int year
    ) {
        var baseResponse = new BaseResponseDTO<List<BookingChartResponseDTO>>();
        List<BookingChartResponseDTO> chart = rentalBookingService.getBookingChart(period, year);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setMessage("Booking chart retrieved successfully");
        baseResponse.setTimestamp(new Date());
        baseResponse.setData(chart);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
