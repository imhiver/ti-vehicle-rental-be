package apap.ti._5.vehicle_rental_2306165553_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.CommandLineRunner;

import apap.ti._5.vehicle_rental_2306165553_be.model.RentalVendor;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalVendorRepository;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalAddOnRepository;
import apap.ti._5.vehicle_rental_2306165553_be.restservice.ListService;
import com.github.javafaker.Faker;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.UUID;
import java.util.TimeZone;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
public class VehicleRental2306165553BeApplication {

	@PostConstruct
	public void init() {
		// Set default timezone to Asia/Jakarta (WIB - UTC+7)
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Jakarta"));
	}

	public static void main(String[] args) {
		SpringApplication.run(VehicleRental2306165553BeApplication.class, args);
	}

	@Bean
	public CommandLineRunner createDummyRentalVendors(RentalVendorRepository rentalVendorRepository, RentalAddOnRepository rentalAddOnRepository, ListService listService) {
		return args -> {
			System.out.println("Generating dummy rental vendors...");
			Faker faker = new Faker(Locale.of("id_ID"));
			List<String> locations = listService.getLocationList();

			if (locations == null || locations.isEmpty()) {
				System.out.println("No locations available, skipping dummy rental vendor generation.");
			} else {
				for (int i = 0; i < 10; i++) {
					RentalVendor rentalVendor = new RentalVendor();
					rentalVendor.setName(faker.company().name());
					rentalVendor.setEmail(faker.internet().emailAddress());
					rentalVendor.setPhone(faker.phoneNumber().phoneNumber());
					
					List<String> selectedLocations = new ArrayList<>();
					for (int j = 0; j < 3; j++) {
						selectedLocations.add(locations.get(faker.random().nextInt(locations.size())));
					}
					rentalVendor.setListOfLocations(selectedLocations);

					rentalVendorRepository.save(rentalVendor);
				}
				System.out.println("Dummy rental vendor data generation complete.");
			}

			System.out.println("Generating dummy rental add-ons...");
			for (int i = 0; i < 10; i++) {
				RentalAddOn rentalAddOn = new RentalAddOn();
				rentalAddOn.setId(UUID.randomUUID());
				rentalAddOn.setName(faker.commerce().productName());
				double price = faker.number().numberBetween(10000, 500000);
				rentalAddOn.setPrice(price);

				rentalAddOnRepository.save(rentalAddOn);
			}
			System.out.println("Dummy rental add-on data generation complete.");
		};
	}
		
}
