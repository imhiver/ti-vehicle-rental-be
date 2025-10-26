package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalVendor;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalAddOnRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalVendorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ListServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RentalVendorRepository rentalVendorRepository;

    @Mock
    private RentalAddOnRepository rentalAddOnRepository;

    @InjectMocks
    private ListServiceImpl listService;

    @Test
    void testGetLocationList() {
        String url = "https://wilayah.id/api/provinces.json";
        Map<String, Object> mockResponse = Map.of(
            "data", List.of(
                Map.of("name", "Jakarta"),
                Map.of("name", "Jawa Barat")
            )
        );
        Mockito.when(restTemplate.getForObject(url, Map.class)).thenReturn(mockResponse);

        List<String> locations = listService.getLocationList();
        assertEquals(List.of("Jakarta", "Jawa Barat"), locations);
    }

    @Test
    void testGetAllRentalVendorNames() {
        RentalVendor vendor1 = new RentalVendor();
        vendor1.setName("VendorA");
        RentalVendor vendor2 = new RentalVendor();
        vendor2.setName("VendorB");
        Mockito.when(rentalVendorRepository.findAll()).thenReturn(List.of(vendor1, vendor2));

        List<String> names = listService.getAllRentalVendorNames();
        assertEquals(List.of("VendorA", "VendorB"), names);
    }

    @Test
    void testGetVehicleTypeOptions() {
        List<String> types = listService.getVehicleTypeOptions();
        assertEquals(List.of("Sedan", "SUV", "MPV", "Luxury"), types);
    }

    @Test
    void testGetTransmissionOptions() {
        List<String> transmissions = listService.getTransmissionOptions();
        assertEquals(List.of("Manual", "Automatic"), transmissions);
    }

    @Test
    void testGetFuelTypeOptions() {
        List<String> fuels = listService.getFuelTypeOptions();
        assertEquals(List.of("Bensin", "Diesel", "Hybrid", "Listrik"), fuels);
    }

    @Test
    void testGetVehicleStatusOptions() {
        List<String> statuses = listService.getVehicleStatusOptions();
        assertEquals(List.of("Available", "In Use", "Unavailable"), statuses);
    }

    @Test
    void testGetBookingStatusOptions() {
        List<String> bookingStatuses = listService.getBookingStatusOptions();
        assertEquals(List.of("Upcoming", "Ongoing", "Done"), bookingStatuses);
    }

    @Test
    void testGetAllAddOns() {
        RentalAddOn addOn1 = new RentalAddOn();
        RentalAddOn addOn2 = new RentalAddOn();
        Mockito.when(rentalAddOnRepository.findAll()).thenReturn(List.of(addOn1, addOn2));

        List<RentalAddOn> addOns = listService.getAllAddOns();
        assertEquals(2, addOns.size());
        assertTrue(addOns.contains(addOn1));
        assertTrue(addOns.contains(addOn2));
    }
}
