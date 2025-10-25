package apap.ti._5.vehicle_rental_2306165553_be.repository;

import apap.ti._5.vehicle_rental_2306165553_be.model.RentalBooking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalBookingRepository extends JpaRepository<RentalBooking, String> {

    List<RentalBooking> findByVehicle_IdAndStatusIn(String id, List<String> of);
   
}
