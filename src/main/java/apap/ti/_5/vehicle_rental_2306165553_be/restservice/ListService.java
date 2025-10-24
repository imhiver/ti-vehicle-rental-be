package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ListService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    public List<String> getProvinsiList() {
        String url = "https://wilayah.id/api/provinces.json";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Map<String, String>> data = (List<Map<String, String>>) response.get("data");
        return data.stream().map(m -> m.get("name")).collect(Collectors.toList());
    }

    public List<String> getVehicleTypeOptions() {
        return List.of("Sedan", "SUV", "MPV", "Luxury");
    }

    public List<String> getTransmissionOptions() {
        return List.of("Manual", "Automatic");
    }

    public List<String> getFuelTypeOptions() {
        return List.of("Bensin", "Diesel", "Hybrid", "Listrik");
    }

    public List<String> getVehicleStatusOptions() {
        return List.of("Available", "In Use", "Unavailable");
    }

    public List<String> getBookingStatusOptions() {
        return List.of("Upcoming", "Ongoing", "Done");
    }

}
