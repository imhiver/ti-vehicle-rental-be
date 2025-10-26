package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.VehicleService;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.CreateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.UpdateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.SearchVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleSearchResultDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllVehicles() throws Exception {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId("VEH0001");
        dto.setBrand("Toyota");
        List<VehicleResponseDTO> list = List.of(dto);

        Mockito.when(vehicleService.getAllVehicle(null, null)).thenReturn(list);

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("VEH0001"))
                .andExpect(jsonPath("$.data[0].brand").value("Toyota"));
    }

    @Test
    void testGetVehicleById_found() throws Exception {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId("VEH0001");
        dto.setBrand("Toyota");

        Mockito.when(vehicleService.getVehicleById("VEH0001")).thenReturn(dto);

        mockMvc.perform(get("/vehicles/VEH0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VEH0001"))
                .andExpect(jsonPath("$.data.brand").value("Toyota"));
    }

    @Test
    void testGetVehicleById_notFound() throws Exception {
        Mockito.when(vehicleService.getVehicleById("VEH9999")).thenThrow(new Exception("Vehicle not found"));

        mockMvc.perform(get("/vehicles/VEH9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Vehicle not found")));
    }

    @Test
    void testCreateVehicle_success() throws Exception {
        CreateVehicleRequestDTO req = new CreateVehicleRequestDTO();
        req.setType("SUV");
        req.setBrand("Toyota");
        req.setModel("Fortuner");
        req.setProductionYear(2020);
        req.setLocation("Jakarta");
        req.setLicencePlate("B1234XYZ");
        req.setCapacity(7);
        req.setTransmission("Automatic");
        req.setFuelType("Diesel");
        req.setPrice(500000.0);
        req.setRentalVendorName("VendorA");

        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId("VEH0001");
        dto.setBrand("Toyota");
        dto.setType("SUV");

        Mockito.when(vehicleService.createVehicle(any(CreateVehicleRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/vehicles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VEH0001"))
                .andExpect(jsonPath("$.data.brand").value("Toyota"));
    }

    @Test
    void testCreateVehicle_invalid() throws Exception {
        CreateVehicleRequestDTO req = new CreateVehicleRequestDTO(); 
        mockMvc.perform(post("/vehicles/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateVehicle_success() throws Exception {
        UpdateVehicleRequestDTO req = new UpdateVehicleRequestDTO();
        req.setType("SUV");
        req.setBrand("Toyota");
        req.setModel("Fortuner");
        req.setProductionYear(2020);
        req.setLocation("Jakarta");
        req.setLicencePlate("B1234XYZ");
        req.setCapacity(7);
        req.setTransmission("Automatic");
        req.setFuelType("Diesel");
        req.setPrice(500000.0);
        req.setStatus("Available");
        req.setRentalVendorName("VendorA");

        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId("VEH0001");
        dto.setBrand("Toyota");
        dto.setType("SUV");

        Mockito.when(vehicleService.updateVehicle(eq("VEH0001"), any(UpdateVehicleRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/vehicles/VEH0001/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("VEH0001"))
                .andExpect(jsonPath("$.data.brand").value("Toyota"));
    }

    @Test
    void testUpdateVehicle_invalid() throws Exception {
        UpdateVehicleRequestDTO req = new UpdateVehicleRequestDTO(); 

        mockMvc.perform(put("/vehicles/VEH0001/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteVehicle_success() throws Exception {
        Mockito.doNothing().when(vehicleService).deleteVehicle("VEH0001");

        mockMvc.perform(delete("/vehicles/VEH0001/delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vehicle deleted successfully"));
    }

    @Test
    void testDeleteVehicle_error() throws Exception {
        Mockito.doThrow(new Exception("Kendaraan sedang disewa")).when(vehicleService).deleteVehicle("VEH0001");

        mockMvc.perform(delete("/vehicles/VEH0001/delete"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Failed to delete vehicle")));
    }

    @Test
    void testSearchVehicles_success() throws Exception {
        SearchVehicleRequestDTO req = new SearchVehicleRequestDTO();
        req.setPickUpLocation("Jakarta");
        req.setDropOffLocation("Bandung");
        req.setCapacityNeeded(5);
        req.setTransmissionNeeded("Automatic");
        req.setIncludeDriver(true);

        VehicleSearchResultDTO result = new VehicleSearchResultDTO();
        result.setId("VEH0001");
        result.setTotalPrice(1000000.0);

        Mockito.when(vehicleService.searchAvailableVehicles(any(SearchVehicleRequestDTO.class)))
                .thenReturn(List.of(result));

        mockMvc.perform(post("/vehicles/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("VEH0001"))
                .andExpect(jsonPath("$.data[0].totalPrice").value(1000000.0));
    }
}
