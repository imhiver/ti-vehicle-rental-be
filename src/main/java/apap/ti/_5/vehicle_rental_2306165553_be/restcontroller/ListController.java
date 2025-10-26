
package apap.ti._5.vehicle_rental_2306165553_be.restcontroller;

import apap.ti._5.vehicle_rental_2306165553_be.restservice.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<List<String>> getAddOns() {
        List<String> addOnNames = listService.getAllAddOns()
            .stream()
            .map(addOn -> addOn.getName())
            .toList();
        return ResponseEntity.ok(addOnNames);
    }

}