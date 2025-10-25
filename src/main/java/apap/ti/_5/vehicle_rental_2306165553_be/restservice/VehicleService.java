package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import java.util.List;

import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.CreateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleResponseDTO;

public interface VehicleService {
    List<VehicleResponseDTO> getAllVehicle(String search, String filterByType);
    VehicleResponseDTO createVehicle(CreateVehicleRequestDTO dto) throws Exception;
}
