package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.ListService;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.RentalVendorListDTO;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListService listService;

    @Test
    void testGetLocations() throws Exception {
        List<String> locations = List.of("Jakarta", "Bandung", "Surabaya");
        when(listService.getLocationList()).thenReturn(locations);

        mockMvc.perform(get("/list/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success get locations"))
                .andExpect(jsonPath("$.data[0]").value("Jakarta"))
                .andExpect(jsonPath("$.data[1]").value("Bandung"))
                .andExpect(jsonPath("$.data[2]").value("Surabaya"));
    }

    @Test
    void testGetRentalVendors() throws Exception {
        List<RentalVendorListDTO> vendors = List.of(
            new RentalVendorListDTO("Vendor A", List.of("Jakarta", "Bandung")),
            new RentalVendorListDTO("Vendor B", List.of("Surabaya", "Malang"))
        );
        when(listService.getAllRentalVendorDTOs()).thenReturn(vendors);

        mockMvc.perform(get("/list/rental-vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success get rental vendors"))
                .andExpect(jsonPath("$.data[0].name").value("Vendor A"))
                .andExpect(jsonPath("$.data[0].listOfLocations[0]").value("Jakarta"))
                .andExpect(jsonPath("$.data[0].listOfLocations[1]").value("Bandung"))
                .andExpect(jsonPath("$.data[1].name").value("Vendor B"))
                .andExpect(jsonPath("$.data[1].listOfLocations[0]").value("Surabaya"))
                .andExpect(jsonPath("$.data[1].listOfLocations[1]").value("Malang"));
    }

    @Test
    void testGetAddOns() throws Exception {
        RentalAddOn addOn1 = new RentalAddOn();
        addOn1.setName("GPS");
        addOn1.setPrice(50000.0);
        
        RentalAddOn addOn2 = new RentalAddOn();
        addOn2.setName("Child Seat");
        addOn2.setPrice(25000.0);
        
        List<RentalAddOn> addOns = List.of(addOn1, addOn2);
        when(listService.getAllAddOns()).thenReturn(addOns);

        mockMvc.perform(get("/list/add-ons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Success get add-ons"))
                .andExpect(jsonPath("$.data[0].name").value("GPS"))
                .andExpect(jsonPath("$.data[0].price").value(50000.0))
                .andExpect(jsonPath("$.data[1].name").value("Child Seat"))
                .andExpect(jsonPath("$.data[1].price").value(25000.0));
    }
}
