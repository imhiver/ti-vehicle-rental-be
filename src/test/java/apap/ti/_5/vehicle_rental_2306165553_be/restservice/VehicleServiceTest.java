package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.model.Vehicle;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalVendor;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalBooking;
import apap.ti._5.vehicle_rental_2306165553_be.repository.VehicleRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalVendorRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalBookingRepository;
import apap.ti._5.vehicle_rental_2306165553_be.restservice.ListService;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.CreateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.UpdateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.SearchVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleSearchResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RentalVendorRepository rentalVendorRepository;

    @Mock
    private RentalBookingRepository rentalBookingRepository;

    @Mock
    private ListService listService;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle;
    private RentalVendor vendor;

    @BeforeEach
    void setUp() {
        vendor = new RentalVendor();
        vendor.setId(1);
        vendor.setName("VendorA");
        vendor.setListOfLocations(Arrays.asList("Jakarta", "Bandung"));

        vehicle = new Vehicle();
        vehicle.setId("VEH0001");
        vehicle.setRentalVendor(vendor);
        vehicle.setType("SUV");
        vehicle.setBrand("Toyota");
        vehicle.setModel("Fortuner");
        vehicle.setProductionYear(2020);
        vehicle.setLocation("Jakarta");
        vehicle.setLicencePlate("B1234XYZ");
        vehicle.setCapacity(7);
        vehicle.setTransmission("Automatic");
        vehicle.setFuelType("Diesel");
        vehicle.setPrice(500000);
        vehicle.setStatus("Available");
        vehicle.setDeletedAt(null);
    }

    @Test
    void testGetAllVehicle() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        List<VehicleResponseDTO> result = vehicleService.getAllVehicle(null, null);
        assertEquals(1, result.size());
        assertEquals("VEH0001", result.get(0).getId());
    }

    @Test
    void testGetVehicleById_found() throws Exception {
        when(vehicleRepository.findById("VEH0001")).thenReturn(Optional.of(vehicle));
        VehicleResponseDTO dto = vehicleService.getVehicleById("VEH0001");
        assertEquals("VEH0001", dto.getId());
        assertEquals("Toyota", dto.getBrand());
    }

    @Test
    void testGetVehicleById_notFound() {
        when(vehicleRepository.findById("VEH9999")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> vehicleService.getVehicleById("VEH9999"));
        assertTrue(ex.getMessage().contains("Vehicle not found"));
    }

    @Test
    void testCreateVehicle_success() throws Exception {
        CreateVehicleRequestDTO dto = new CreateVehicleRequestDTO();
        dto.setType("SUV");
        dto.setBrand("Toyota");
        dto.setModel("Fortuner");
        dto.setProductionYear(2020);
        dto.setLocation("Jakarta");
        dto.setLicencePlate("B1234XYZ");
        dto.setCapacity(7);
        dto.setTransmission("Automatic");
        dto.setFuelType("Diesel");
        dto.setPrice(500000.0);
        dto.setRentalVendorName("VendorA");

        when(listService.getVehicleTypeOptions()).thenReturn(List.of("SUV", "Sedan"));
        when(listService.getTransmissionOptions()).thenReturn(List.of("Automatic", "Manual"));
        when(listService.getFuelTypeOptions()).thenReturn(List.of("Diesel", "Bensin"));
        when(vehicleRepository.existsByLicencePlate("B1234XYZ")).thenReturn(false);
        when(rentalVendorRepository.findAll()).thenReturn(List.of(vendor)); // <-- Fix: stub vendor lookup
        when(vehicleRepository.findAll(any(Sort.class))).thenReturn(List.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        VehicleResponseDTO response = vehicleService.createVehicle(dto);
        assertEquals("SUV", response.getType());
        assertEquals("Toyota", response.getBrand());
        assertEquals("VendorA", response.getRentalVendorName());
    }

    @Test
    void testCreateVehicle_invalidType() {
        CreateVehicleRequestDTO dto = new CreateVehicleRequestDTO();
        dto.setType("Truck");
        dto.setTransmission("Automatic");
        dto.setFuelType("Diesel");
        dto.setProductionYear(2020);
        dto.setLicencePlate("B1234XYZ");
        dto.setRentalVendorName("VendorA");
        dto.setLocation("Jakarta");

        when(listService.getVehicleTypeOptions()).thenReturn(List.of("SUV", "Sedan"));
        when(listService.getTransmissionOptions()).thenReturn(List.of("Automatic", "Manual"));
        when(listService.getFuelTypeOptions()).thenReturn(List.of("Diesel", "Bensin"));
        when(vehicleRepository.existsByLicencePlate(anyString())).thenReturn(false);
        when(rentalVendorRepository.findAll()).thenReturn(List.of(vendor));

        Exception ex = assertThrows(Exception.class, () -> vehicleService.createVehicle(dto));
        assertTrue(ex.getMessage().contains("Tipe kendaraan tidak valid"));
    }

    @Test
    void testUpdateVehicle_success() throws Exception {
        UpdateVehicleRequestDTO dto = new UpdateVehicleRequestDTO();
        dto.setType("SUV");
        dto.setBrand("Toyota");
        dto.setModel("Fortuner");
        dto.setProductionYear(2020);
        dto.setLocation("Jakarta");
        dto.setLicencePlate("B1234XYZ");
        dto.setCapacity(7);
        dto.setTransmission("Automatic");
        dto.setFuelType("Diesel");
        dto.setPrice(500000.0);
        dto.setStatus("Available");
        dto.setRentalVendorName("VendorA");

        when(vehicleRepository.findById("VEH0001")).thenReturn(Optional.of(vehicle));
        when(listService.getVehicleStatusOptions()).thenReturn(List.of("Available", "In Use", "Unavailable"));
        when(listService.getVehicleTypeOptions()).thenReturn(List.of("SUV", "Sedan"));
        when(listService.getTransmissionOptions()).thenReturn(List.of("Automatic", "Manual"));
        when(listService.getFuelTypeOptions()).thenReturn(List.of("Diesel", "Bensin"));
        when(rentalVendorRepository.findAll()).thenReturn(List.of(vendor));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));

        VehicleResponseDTO response = vehicleService.updateVehicle("VEH0001", dto);
        assertEquals("SUV", response.getType());
        assertEquals("Toyota", response.getBrand());
    }

    @Test
    void testUpdateVehicle_inUse() {
        vehicle.setStatus("In Use");
        UpdateVehicleRequestDTO dto = new UpdateVehicleRequestDTO();
        dto.setType("SUV");
        dto.setTransmission("Automatic");
        dto.setFuelType("Diesel");
        dto.setProductionYear(2020);
        dto.setLicencePlate("B1234XYZ");
        dto.setRentalVendorName("VendorA");
        dto.setLocation("Jakarta");
        dto.setStatus("Available");

        when(vehicleRepository.findById("VEH0001")).thenReturn(Optional.of(vehicle));
        when(listService.getVehicleStatusOptions()).thenReturn(List.of("Available", "In Use", "Unavailable"));
        when(listService.getVehicleTypeOptions()).thenReturn(List.of("SUV", "Sedan"));
        when(listService.getTransmissionOptions()).thenReturn(List.of("Automatic", "Manual"));
        when(listService.getFuelTypeOptions()).thenReturn(List.of("Diesel", "Bensin"));
        when(rentalVendorRepository.findAll()).thenReturn(List.of(vendor));

        Exception ex = assertThrows(Exception.class, () -> vehicleService.updateVehicle("VEH0001", dto));
        assertTrue(ex.getMessage().contains("Kendaraan sedang disewa"));
    }

    @Test
    void testDeleteVehicle_success() throws Exception {
        when(vehicleRepository.findById("VEH0001")).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(inv -> inv.getArgument(0));
        vehicleService.deleteVehicle("VEH0001");
        assertEquals("Unavailable", vehicle.getStatus());
        assertNotNull(vehicle.getDeletedAt());
    }

    @Test
    void testDeleteVehicle_inUse() {
        vehicle.setStatus("In Use");
        when(vehicleRepository.findById("VEH0001")).thenReturn(Optional.of(vehicle));
        Exception ex = assertThrows(Exception.class, () -> vehicleService.deleteVehicle("VEH0001"));
        assertTrue(ex.getMessage().contains("Kendaraan sedang disewa"));
    }

    @Test
    void testSearchAvailableVehicles() {
        SearchVehicleRequestDTO dto = new SearchVehicleRequestDTO();
        dto.setPickUpLocation("Jakarta");
        dto.setDropOffLocation("Bandung");
        dto.setCapacityNeeded(5);
        dto.setTransmissionNeeded("Automatic");
        dto.setPickUpTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        dto.setDropOffTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48));
        dto.setIncludeDriver(true);

        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));
        when(rentalBookingRepository.findByVehicle_IdAndStatusIn(anyString(), anyList())).thenReturn(Collections.emptyList());

        List<VehicleSearchResultDTO> results = vehicleService.searchAvailableVehicles(dto);
        assertEquals(1, results.size());
        assertEquals("VEH0001", results.get(0).getId());
        assertTrue(results.get(0).getTotalPrice() > 0);
    }
}
