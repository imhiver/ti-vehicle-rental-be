package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import java.util.List;

public interface ListService {
    List<String> getLocationList();
    List<String> getAllRentalVendorNames();
    List<String> getVehicleTypeOptions();
    List<String> getTransmissionOptions();
    List<String> getFuelTypeOptions();
    List<String> getVehicleStatusOptions();
    List<String> getBookingStatusOptions();
}
