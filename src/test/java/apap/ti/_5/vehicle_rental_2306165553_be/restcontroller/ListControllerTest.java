package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.ListService;
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
                .andExpect(jsonPath("$[0]").value("Jakarta"))
                .andExpect(jsonPath("$[1]").value("Bandung"))
                .andExpect(jsonPath("$[2]").value("Surabaya"));
    }

    @Test
    void testGetRentalVendors() throws Exception {
        List<String> vendors = List.of("Vendor A", "Vendor B");
        when(listService.getAllRentalVendorNames()).thenReturn(vendors);

        mockMvc.perform(get("/list/rental-vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Vendor A"))
                .andExpect(jsonPath("$[1]").value("Vendor B"));
    }
}
