package apap.ti._5.vehicle_rental_2306165553_be.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalVendorListDTO {
    private String name;
    private List<String> listOfLocations;
}
