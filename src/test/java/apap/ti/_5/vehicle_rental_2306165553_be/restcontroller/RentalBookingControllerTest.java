package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.RentalBookingService;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.RentalBookingResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.BookingChartResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.CreateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingAddOnsRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingStatusRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateRentalBookingRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RentalBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalBookingService rentalBookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllBookings() throws Exception {
        RentalBookingResponseDTO dto = new RentalBookingResponseDTO();
        dto.setId("VR0001");
        dto.setStatus("Upcoming");
        List<RentalBookingResponseDTO> list = List.of(dto);

        Mockito.when(rentalBookingService.getAllBookings(null)).thenReturn(list);

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("VR0001"))
                .andExpect(jsonPath("$.data[0].status").value("Upcoming"));
    }

    @Test
    void testGetBookingById_found() throws Exception {
        RentalBookingResponseDTO dto = new RentalBookingResponseDTO();
        dto.setId("VR0001");
        dto.setStatus("Upcoming");

        Mockito.when(rentalBookingService.getBookingById("VR0001")).thenReturn(dto);

        mockMvc.perform(get("/bookings/VR0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VR0001"))
                .andExpect(jsonPath("$.data.status").value("Upcoming"));
    }

    @Test
    void testGetBookingById_notFound() throws Exception {
        Mockito.when(rentalBookingService.getBookingById("VR9999")).thenThrow(new Exception("Booking not found"));

        mockMvc.perform(get("/bookings/VR9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Booking not found")));
    }

    @Test
    void testCreateBooking_success() throws Exception {
        CreateRentalBookingRequestDTO req = new CreateRentalBookingRequestDTO();
        req.setVehicleId("VEH0001");
        req.setPickUpLocation("Jakarta");
        req.setDropOffLocation("Bandung");
        req.setPickUpTime(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // besok
        req.setDropOffTime(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // lusa
        req.setIncludeDriver(true);
        req.setAddOns(List.of("GPS"));
        req.setCapacityNeeded(1);
        req.setTransmissionNeeded("Automatic");
        req.setBaseTotalPrice(100000.0);

        RentalBookingResponseDTO dto = new RentalBookingResponseDTO();
        dto.setId("VR0002");
        dto.setStatus("Upcoming");

        Mockito.when(rentalBookingService.createBooking(any(CreateRentalBookingRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/bookings/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VR0002"))
                .andExpect(jsonPath("$.data.status").value("Upcoming"));
    }

    @Test
    void testCreateBooking_invalid() throws Exception {
        CreateRentalBookingRequestDTO req = new CreateRentalBookingRequestDTO();
        mockMvc.perform(post("/bookings/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateBooking_success() throws Exception {
        UpdateRentalBookingRequestDTO req = new UpdateRentalBookingRequestDTO();
        req.setVehicleId("VEH0001");
        req.setPickUpLocation("Jakarta");
        req.setDropOffLocation("Bandung");
        req.setPickUpTime(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        req.setDropOffTime(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48));
        req.setIncludeDriver(true);
        req.setTransmissionNeeded("Automatic");
        req.setCapacityNeeded(1);
        req.setBaseTotalPrice(200000.0);

        RentalBookingResponseDTO dto = new RentalBookingResponseDTO();
        dto.setId("VR0001");
        dto.setStatus("Upcoming");

        Mockito.when(rentalBookingService.updateBooking(eq("VR0001"), any(UpdateRentalBookingRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/bookings/VR0001/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VR0001"))
                .andExpect(jsonPath("$.data.status").value("Upcoming"));
    }

    @Test
    void testUpdateBooking_invalid() throws Exception {
        UpdateRentalBookingRequestDTO req = new UpdateRentalBookingRequestDTO();
        mockMvc.perform(put("/bookings/VR0001/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateBookingStatus_success() throws Exception {
        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Ongoing");

        RentalBookingResponseDTO dto = new RentalBookingResponseDTO();
        dto.setId("VR0001");
        dto.setStatus("Ongoing");

        Mockito.when(rentalBookingService.updateBookingStatus(any(UpdateBookingStatusRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/bookings/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VR0001"))
                .andExpect(jsonPath("$.data.status").value("Ongoing"));
    }

    @Test
    void testUpdateBookingStatus_invalid() throws Exception {
        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        mockMvc.perform(put("/bookings/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateBookingAddOns_success() throws Exception {
        UpdateBookingAddOnsRequestDTO req = new UpdateBookingAddOnsRequestDTO();
        req.setBookingId("VR0001");
        req.setAddOns(List.of("GPS"));

        RentalBookingResponseDTO dto = new RentalBookingResponseDTO();
        dto.setId("VR0001");
        dto.setStatus("Upcoming");

        Mockito.when(rentalBookingService.updateBookingAddOns(any(UpdateBookingAddOnsRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/bookings/update-addons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VR0001"))
                .andExpect(jsonPath("$.data.status").value("Upcoming"));
    }

    @Test
    void testUpdateBookingAddOns_invalid() throws Exception {
        UpdateBookingAddOnsRequestDTO req = new UpdateBookingAddOnsRequestDTO();
        mockMvc.perform(put("/bookings/update-addons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteBooking_success() throws Exception {
        Mockito.doNothing().when(rentalBookingService).cancelBooking("VR0001");

        mockMvc.perform(delete("/bookings/VR0001/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Booking canceled successfully"));
    }

    @Test
    void testDeleteBooking_error() throws Exception {
        Mockito.doThrow(new Exception("Booking tidak bisa dibatalkan")).when(rentalBookingService).cancelBooking("VR0001");

        mockMvc.perform(delete("/bookings/VR0001/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Failed to cancel booking")));
    }

    @Test
    void testGetBookingChart_success() throws Exception {
        BookingChartResponseDTO chartDto = new BookingChartResponseDTO();
        chartDto.setLabel("Jan");
        chartDto.setTotalBookings(5);

        Mockito.when(rentalBookingService.getBookingChart("monthly", 2024)).thenReturn(List.of(chartDto));

        mockMvc.perform(get("/bookings/chart?period=monthly&year=2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].label").value("Jan"))
                .andExpect(jsonPath("$.data[0].totalBookings").value(5));
    }
}
