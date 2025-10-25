package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalVendor;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalVendorRepository;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalAddOnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RentalVendorRepository rentalVendorRepository;

    @Autowired
    private RentalAddOnRepository rentalAddOnRepository;

    @Override
    public List<String> getLocationList() {
        String url = "https://wilayah.id/api/provinces.json";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, String>> data = (List<Map<String, String>>) response.get("data");
        return data.stream().map(m -> m.get("name")).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllRentalVendorNames() {
        return rentalVendorRepository.findAll()
            .stream()
            .map(v -> v.getName())
            .toList();
    }

    @Override
    public List<String> getVehicleTypeOptions() {
        return List.of("Sedan", "SUV", "MPV", "Luxury");
    }

    @Override
    public List<String> getTransmissionOptions() {
        return List.of("Manual", "Automatic");
    }

    @Override
    public List<String> getFuelTypeOptions() {
        return List.of("Bensin", "Diesel", "Hybrid", "Listrik");
    }

    @Override
    public List<String> getVehicleStatusOptions() {
        return List.of("Available", "In Use", "Unavailable");
    }

    @Override
    public List<String> getBookingStatusOptions() {
        return List.of("Upcoming", "Ongoing", "Done");
    }

    @Override
    public List<RentalAddOn> getAllAddOns() {
        return rentalAddOnRepository.findAll();
    }
}
