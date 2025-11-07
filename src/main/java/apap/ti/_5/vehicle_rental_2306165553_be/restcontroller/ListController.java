package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.ListService;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.BaseResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.RentalVendorListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    private ListService listService;

    @GetMapping("/locations")
    public ResponseEntity<BaseResponseDTO<List<String>>> getLocations() {
        List<String> locations = listService.getLocationList();
        BaseResponseDTO<List<String>> response = new BaseResponseDTO<>(
            200,
            "Success get locations",
            new Date(),
            locations
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rental-vendors")
    public ResponseEntity<BaseResponseDTO<List<RentalVendorListDTO>>> getRentalVendors() {
        List<RentalVendorListDTO> vendors = listService.getAllRentalVendorDTOs();
        BaseResponseDTO<List<RentalVendorListDTO>> response = new BaseResponseDTO<>(
            200,
            "Success get rental vendors",
            new Date(),
            vendors
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/add-ons")
    public ResponseEntity<BaseResponseDTO<List<Map<String, Object>>>> getAddOns() {
        List<Map<String, Object>> addOnMaps = listService.getAllAddOns()
            .stream()
            .map(addOn -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("name", addOn.getName());
                map.put("price", addOn.getPrice());
                return map;
            })
            .collect(Collectors.toList());
        BaseResponseDTO<List<Map<String, Object>>> response = new BaseResponseDTO<>(
            200,
            "Success get add-ons",
            new Date(),
            addOnMaps
        );
        return ResponseEntity.ok(response);
    }

}