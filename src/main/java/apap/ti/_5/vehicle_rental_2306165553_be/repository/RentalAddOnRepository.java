package apap.ti._5.vehicle_rental_2306165553_be.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;

public interface RentalAddOnRepository extends JpaRepository<RentalAddOn, UUID> {

    Optional<RentalAddOn> findByName(String addOnName);

}
