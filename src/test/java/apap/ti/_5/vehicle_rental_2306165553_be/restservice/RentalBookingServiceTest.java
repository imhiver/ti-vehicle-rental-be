package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.RentalBookingServiceImpl;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalBookingRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.VehicleRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalAddOnRepository;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalBooking;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalVendor;
import apap.ti._5.vehicle_rental_2306165553_be.model.Vehicle;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.*;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalBookingServiceTest {

    @Mock
    private RentalBookingRepository rentalBookingRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RentalAddOnRepository rentalAddOnRepository;

    @InjectMocks
    private RentalBookingServiceImpl rentalBookingService;

    private RentalBooking booking;
    private Vehicle vehicle;
    private RentalVendor vendor;

    @BeforeEach
    void setUp() {
        booking = new RentalBooking();
        booking.setId("VR0001");
        booking.setStatus("Upcoming");
        booking.setPickUpLocation("Jakarta");
        booking.setDropOffLocation("Bandung");
        booking.setTotalPrice(100000.0);
        booking.setPickUpTime(new Date());
        booking.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));

        vendor = new RentalVendor();
        vendor.setId(1);
        vendor.setName("VendorA");
        vendor.setListOfLocations(Arrays.asList("Jakarta", "Bandung"));

        vehicle = new Vehicle();
        vehicle.setId("VEH0001");
        vehicle.setBrand("Toyota");
        vehicle.setTransmission("Automatic");
        vehicle.setCapacity(5);
        vehicle.setPrice(100000.0);
        vehicle.setRentalVendor(vendor); 
    }

    @Test
    void testGetAllBookings() {
        when(rentalBookingRepository.findAll()).thenReturn(List.of(booking));
        List<RentalBookingResponseDTO> result = rentalBookingService.getAllBookings(null);
        assertEquals(1, result.size());
        assertEquals("VR0001", result.get(0).getId());
    }

    @Test
    void testGetBookingById_found() throws Exception {
        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        RentalBookingResponseDTO dto = rentalBookingService.getBookingById("VR0001");
        assertEquals("VR0001", dto.getId());
        assertEquals("Upcoming", dto.getStatus());
    }

    @Test
    void testGetBookingById_notFound() {
        when(rentalBookingRepository.findById("VR9999")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.getBookingById("VR9999"));
        assertTrue(ex.getMessage().contains("Booking not found"));
    }

    @Test
    void testCreateBooking_success() throws Exception {
        CreateRentalBookingRequestDTO req = new CreateRentalBookingRequestDTO();
        req.setVehicleId("VEH0001");
        req.setPickUpLocation("Jakarta");
        req.setDropOffLocation("Bandung");
        req.setPickUpTime(new Date()); 
        req.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        req.setIncludeDriver(true);
        req.setAddOns(List.of("GPS"));
        req.setCapacityNeeded(1);
        req.setTransmissionNeeded("Automatic");
        req.setBaseTotalPrice(100000.0);

        when(vehicleRepository.findById("VEH0001")).thenReturn(Optional.of(vehicle));
        when(rentalAddOnRepository.findByName("GPS")).thenReturn(Optional.of(new RentalAddOn()));
        when(rentalBookingRepository.save(any(RentalBooking.class))).thenAnswer(inv -> {
            RentalBooking b = inv.getArgument(0);
            b.setId("VR0002");
            return b;
        });

        RentalBookingResponseDTO dto = rentalBookingService.createBooking(req);
        assertEquals("VR0002", dto.getId());
        assertEquals("Ongoing", dto.getStatus()); 
    }

    @Test
    void testUpdateBooking_success() throws Exception {
        UpdateRentalBookingRequestDTO req = new UpdateRentalBookingRequestDTO();
        req.setVehicleId("VEH0001");
        req.setPickUpLocation("Jakarta");
        req.setDropOffLocation("Bandung");
        req.setPickUpTime(new Date());
        req.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        req.setIncludeDriver(true);
        req.setTransmissionNeeded("Automatic");
        req.setCapacityNeeded(1);
        req.setBaseTotalPrice(200000.0);

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(vehicleRepository.findById("VEH0001")).thenReturn(Optional.of(vehicle));
        when(rentalBookingRepository.save(any(RentalBooking.class))).thenAnswer(inv -> inv.getArgument(0));

        RentalBookingResponseDTO dto = rentalBookingService.updateBooking("VR0001", req);
        assertEquals("VR0001", dto.getId());
        assertEquals(200000.0, dto.getTotalPrice());
    }

    @Test
    void testUpdateBooking_notFound() {
        UpdateRentalBookingRequestDTO req = new UpdateRentalBookingRequestDTO();
        when(rentalBookingRepository.findById("VR9999")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBooking("VR9999", req));
        assertTrue(ex.getMessage().contains("Booking not found"));
    }

    @Test
    void testUpdateBookingStatus_success() throws Exception {
        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Completed");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalBookingRepository.save(any(RentalBooking.class))).thenAnswer(inv -> {
            RentalBooking b = inv.getArgument(0);
            b.setStatus(req.getNewStatus()); 
            return b;
        });

        RentalBookingResponseDTO dto = rentalBookingService.updateBookingStatus(req);
        assertEquals("VR0001", dto.getId());
        assertEquals("Completed", dto.getStatus());
    }

    @Test
    void testUpdateBookingStatus_notFound() {
        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR9999");
        req.setNewStatus("Completed");
        when(rentalBookingRepository.findById("VR9999")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingStatus(req));
        assertTrue(ex.getMessage().contains("Booking not found"));
    }

    @Test
    void testUpdateBookingAddOns_success() throws Exception {
        UpdateBookingAddOnsRequestDTO req = new UpdateBookingAddOnsRequestDTO();
        req.setBookingId("VR0001");
        req.setAddOns(List.of("GPS", "Child Seat"));

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalAddOnRepository.findByName(anyString())).thenReturn(Optional.of(new RentalAddOn()));
        when(rentalBookingRepository.save(any(RentalBooking.class))).thenAnswer(inv -> inv.getArgument(0));

        RentalBookingResponseDTO dto = rentalBookingService.updateBookingAddOns(req);
        assertEquals("VR0001", dto.getId());
        // assert addOns logic if DTO supports it
    }

    @Test
    void testUpdateBookingAddOns_notFound() {
        UpdateBookingAddOnsRequestDTO req = new UpdateBookingAddOnsRequestDTO();
        req.setBookingId("VR9999");
        req.setAddOns(List.of("GPS"));
        when(rentalBookingRepository.findById("VR9999")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingAddOns(req));
        assertTrue(ex.getMessage().contains("Booking not found"));
    }

    @Test
    void testCancelBooking_success() throws Exception {
        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalBookingRepository.save(any(RentalBooking.class))).thenAnswer(inv -> inv.getArgument(0));
        assertDoesNotThrow(() -> rentalBookingService.cancelBooking("VR0001"));
    }

    @Test
    void testCancelBooking_notFound() {
        when(rentalBookingRepository.findById("VR9999")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.cancelBooking("VR9999"));
        assertTrue(ex.getMessage().contains("Booking not found"));
    }

    @Test
    void testGetBookingChart() {
        List<BookingChartResponseDTO> chart = List.of(new BookingChartResponseDTO());
        List<BookingChartResponseDTO> result = rentalBookingService.getBookingChart("monthly", 2024);
        assertNotNull(result);
    }

    @Test
    void testUpdateBookingStatus_upcomingToOngoing_success() throws Exception {
        booking.setStatus("Upcoming");
        booking.setPickUpTime(new Date(System.currentTimeMillis() - 1000));
        booking.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        vehicle.setStatus("Available");
        vehicle.setLocation("Jakarta");
        booking.setVehicle(vehicle); // fix

        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Ongoing");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalBookingRepository.findByVehicle_IdAndStatusIn(eq("VEH0001"), anyList())).thenReturn(Collections.emptyList());
        when(rentalBookingRepository.save(any(RentalBooking.class))).thenAnswer(inv -> inv.getArgument(0));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        RentalBookingResponseDTO dto = rentalBookingService.updateBookingStatus(req);
        assertEquals("Ongoing", dto.getStatus());
        assertEquals("In Use", vehicle.getStatus());
    }

    @Test
    void testUpdateBookingStatus_upcomingToOngoing_fail_notYetPickupTime() {
        booking.setStatus("Upcoming");
        booking.setPickUpTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        booking.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2));
        vehicle.setStatus("Available");
        vehicle.setLocation("Jakarta");
        booking.setVehicle(vehicle); // fix

        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Ongoing");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalBookingRepository.findByVehicle_IdAndStatusIn(eq("VEH0001"), anyList())).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingStatus(req));
        assertTrue(ex.getMessage().contains("Belum memasuki waktu pengambilan kendaraan"));
    }

    @Test
    void testUpdateBookingStatus_upcomingToOngoing_fail_vehicleNotAvailable() {
        booking.setStatus("Upcoming");
        booking.setPickUpTime(new Date(System.currentTimeMillis() - 1000));
        booking.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        vehicle.setStatus("Unavailable");
        vehicle.setLocation("Jakarta");
        booking.setVehicle(vehicle);

        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Ongoing");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalBookingRepository.findByVehicle_IdAndStatusIn(eq("VEH0001"), anyList())).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingStatus(req));
        assertTrue(ex.getMessage().contains("Kendaraan tidak tersedia"));
    }

    @Test
    void testUpdateBookingStatus_upcomingToOngoing_fail_locationMismatch() {
        booking.setStatus("Upcoming");
        booking.setPickUpTime(new Date(System.currentTimeMillis() - 1000));
        booking.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        vehicle.setStatus("Available");
        vehicle.setLocation("Bandung");
        booking.setVehicle(vehicle); 

        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Ongoing");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalBookingRepository.findByVehicle_IdAndStatusIn(eq("VEH0001"), anyList())).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingStatus(req));
        assertTrue(ex.getMessage().contains("Kendaraan tidak ada di lokasi pengambilan"));
    }

    @Test
    void testUpdateBookingStatus_upcomingToOngoing_fail_ongoingOverlap() {
        booking.setStatus("Upcoming");
        booking.setPickUpTime(new Date(System.currentTimeMillis() - 1000));
        booking.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
        vehicle.setStatus("Available");
        vehicle.setLocation("Jakarta");
        booking.setVehicle(vehicle); 
        RentalBooking otherBooking = new RentalBooking();
        otherBooking.setId("VR0002");
        otherBooking.setPickUpTime(new Date(System.currentTimeMillis() - 1000 * 60 * 60));
        otherBooking.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2));
        otherBooking.setStatus("Ongoing");

        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Ongoing");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(rentalBookingRepository.findByVehicle_IdAndStatusIn(eq("VEH0001"), anyList())).thenReturn(List.of(otherBooking));

        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingStatus(req));
        assertTrue(ex.getMessage().contains("Kendaraan masih digunakan oleh pesanan lain yang Ongoing"));
    }

    @Test
    void testUpdateBookingStatus_upcomingToDone_fail() {
        booking.setStatus("Upcoming");
        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Done");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));

        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingStatus(req));
        assertTrue(ex.getMessage().contains("Tidak bisa mengubah Upcoming menjadi Done"));
    }

    @Test
    void testUpdateBookingStatus_ongoingToDone_success() throws Exception {
        booking.setStatus("Ongoing");
        booking.setPickUpTime(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 2));
        booking.setDropOffTime(new Date(System.currentTimeMillis() - 1000 * 60 * 60)); 
        vehicle.setStatus("In Use");
        vehicle.setLocation("Jakarta");
        booking.setVehicle(vehicle); 

        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Done");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rentalBookingRepository.save(any(RentalBooking.class))).thenAnswer(inv -> inv.getArgument(0));

        double before = booking.getTotalPrice();
        RentalBookingResponseDTO dto = rentalBookingService.updateBookingStatus(req);
        assertEquals("Done", dto.getStatus());
        assertEquals("Available", vehicle.getStatus());
        assertEquals(booking.getDropOffLocation(), vehicle.getLocation());
        assertTrue(dto.getTotalPrice() > before); 
    }

    @Test
    void testUpdateBookingStatus_doneToAny_fail() {
        booking.setStatus("Done");
        UpdateBookingStatusRequestDTO req = new UpdateBookingStatusRequestDTO();
        req.setBookingId("VR0001");
        req.setNewStatus("Ongoing");

        when(rentalBookingRepository.findById("VR0001")).thenReturn(Optional.of(booking));

        Exception ex = assertThrows(Exception.class, () -> rentalBookingService.updateBookingStatus(req));
        assertTrue(ex.getMessage().contains("Status sudah Done"));
    }

    @Test
    void testGetBookingChart_monthly() {
        RentalBooking janBooking = new RentalBooking();
        Calendar janCal = Calendar.getInstance();
        janCal.set(2024, Calendar.JANUARY, 10);
        janBooking.setCreatedAt(janCal.getTime());

        RentalBooking febBooking = new RentalBooking();
        Calendar febCal = Calendar.getInstance();
        febCal.set(2024, Calendar.FEBRUARY, 15);
        febBooking.setCreatedAt(febCal.getTime());

        RentalBooking marBooking = new RentalBooking();
        Calendar marCal = Calendar.getInstance();
        marCal.set(2024, Calendar.MARCH, 20);
        marBooking.setCreatedAt(marCal.getTime());

        when(rentalBookingRepository.findAll()).thenReturn(List.of(janBooking, febBooking, marBooking));

        List<BookingChartResponseDTO> chart = rentalBookingService.getBookingChart("monthly", 2024);
        assertEquals(12, chart.size());
        assertEquals(1, chart.get(0).getTotalBookings()); // Jan
        assertEquals(1, chart.get(1).getTotalBookings()); // Feb
        assertEquals(1, chart.get(2).getTotalBookings()); // Mar
        assertEquals("Jan", chart.get(0).getLabel());
        assertEquals("Feb", chart.get(1).getLabel());
        assertEquals("Mar", chart.get(2).getLabel());
    }

    @Test
    void testGetBookingChart_quarterly() {
        RentalBooking janBooking = new RentalBooking();
        Calendar janCal = Calendar.getInstance();
        janCal.set(2024, Calendar.JANUARY, 10);
        janBooking.setCreatedAt(janCal.getTime());

        RentalBooking aprBooking = new RentalBooking();
        Calendar aprCal = Calendar.getInstance();
        aprCal.set(2024, Calendar.APRIL, 15);
        aprBooking.setCreatedAt(aprCal.getTime());

        RentalBooking julBooking = new RentalBooking();
        Calendar julCal = Calendar.getInstance();
        julCal.set(2024, Calendar.JULY, 20);
        julBooking.setCreatedAt(julCal.getTime());

        RentalBooking octBooking = new RentalBooking();
        Calendar octCal = Calendar.getInstance();
        octCal.set(2024, Calendar.OCTOBER, 5);
        octBooking.setCreatedAt(octCal.getTime());

        when(rentalBookingRepository.findAll()).thenReturn(List.of(janBooking, aprBooking, julBooking, octBooking));

        List<BookingChartResponseDTO> chart = rentalBookingService.getBookingChart("quarterly", 2024);
        assertEquals(4, chart.size());
        assertEquals(1, chart.get(0).getTotalBookings()); // Q1
        assertEquals(1, chart.get(1).getTotalBookings()); // Q2
        assertEquals(1, chart.get(2).getTotalBookings()); // Q3
        assertEquals(1, chart.get(3).getTotalBookings()); // Q4
        assertEquals("Q1", chart.get(0).getLabel());
        assertEquals("Q2", chart.get(1).getLabel());
        assertEquals("Q3", chart.get(2).getLabel());
        assertEquals("Q4", chart.get(3).getLabel());
    }

    @Test
    void testGetBookingChart_filterYear() {
        RentalBooking booking2023 = new RentalBooking();
        Calendar cal2023 = Calendar.getInstance();
        cal2023.set(2023, Calendar.JANUARY, 10);
        booking2023.setCreatedAt(cal2023.getTime());

        RentalBooking booking2024 = new RentalBooking();
        Calendar cal2024 = Calendar.getInstance();
        cal2024.set(2024, Calendar.JANUARY, 10);
        booking2024.setCreatedAt(cal2024.getTime());

        when(rentalBookingRepository.findAll()).thenReturn(List.of(booking2023, booking2024));

        List<BookingChartResponseDTO> chart = rentalBookingService.getBookingChart("monthly", 2024);
        assertEquals(12, chart.size());
        assertEquals(1, chart.get(0).getTotalBookings()); 
        for (int i = 1; i < 12; i++) {
            assertEquals(0, chart.get(i).getTotalBookings());
        }
    }
}
