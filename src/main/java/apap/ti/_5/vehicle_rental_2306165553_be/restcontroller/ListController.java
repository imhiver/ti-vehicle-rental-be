package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;
import apap.ti._5.vehicle_rental_2306165553_be.restservice.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
public class ListController {

    @Autowired
    private ListService listService;

    @GetMapping("/locations")
    public ResponseEntity<List<String>> getLocations() {
        return ResponseEntity.ok(listService.getLocationList());
    }

    @GetMapping("/rental-vendors")
    public ResponseEntity<List<String>> getRentalVendors() {
        return ResponseEntity.ok(listService.getAllRentalVendorNames());
    }

    @GetMapping("/add-ons")
    public ResponseEntity<List<Map<String, Object>>> getAddOns() {
        List<Map<String, Object>> addOnMaps = listService.getAllAddOns()
            .stream()
            .map(addOn -> {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("name", addOn.getName());
                map.put("price", addOn.getPrice());
                return map;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(addOnMaps);
    }

}