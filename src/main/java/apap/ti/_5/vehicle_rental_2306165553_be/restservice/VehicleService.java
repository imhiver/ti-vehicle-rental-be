package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import java.util.List;

import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.CreateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.UpdateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.SearchVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleSearchResultDTO;

public interface VehicleService {
    List<VehicleResponseDTO> getAllVehicle(String search, String filterByType);
    VehicleResponseDTO getVehicleById(String id) throws Exception;
    VehicleResponseDTO createVehicle(CreateVehicleRequestDTO dto) throws Exception;
    VehicleResponseDTO updateVehicle(String id, UpdateVehicleRequestDTO dto) throws Exception;
    void deleteVehicle(String id) throws Exception;
    List<VehicleSearchResultDTO> searchAvailableVehicles(SearchVehicleRequestDTO dto, String excludeBookingId);
}
