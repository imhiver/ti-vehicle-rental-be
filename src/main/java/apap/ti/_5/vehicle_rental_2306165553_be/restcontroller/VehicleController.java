package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.VehicleService;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.CreateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.BaseResponseDTO;
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
@RequestMapping("/vehicles")
public class VehicleController {
    public static final String VIEW_VEHICLE_DETAIL =  "/{id}";
    public static final String CREATE_VEHICLE =  "/create";
    public static final String UPDATE_VEHICLE =  "/update";
    public static final String DELETE_VEHICLE =  "/delete";

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("")
    public ResponseEntity<BaseResponseDTO<List<VehicleResponseDTO>>> getAllVehicles(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filterByType) {
        var baseResponseDTO = new BaseResponseDTO<List<VehicleResponseDTO>>();

        List<VehicleResponseDTO> vehicles = vehicleService.getAllVehicle(search, filterByType);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("Vehicles retrieved successfully");
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(vehicles);
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PostMapping(CREATE_VEHICLE)
    public ResponseEntity<BaseResponseDTO<VehicleResponseDTO>> createVehicle(
            @Valid @RequestBody CreateVehicleRequestDTO dto,
            BindingResult bindingResult) {

        var baseResponseDTO = new BaseResponseDTO<VehicleResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.append(error.getDefaultMessage()).append("; ");
            }
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages.toString());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        VehicleResponseDTO vehicle;
        try {
            vehicle = vehicleService.createVehicle(dto);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Failed to create vehicle: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("Vehicle created successfully");
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(vehicle);
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
