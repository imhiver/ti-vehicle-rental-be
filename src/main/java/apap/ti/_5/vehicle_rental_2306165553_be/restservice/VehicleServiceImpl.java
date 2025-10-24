package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.model.Vehicle;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalVendor;
import apap.ti._5.vehicle_rental_2306165553_be.repository.VehicleRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalVendorRepository;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.CreateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.time.Year;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RentalVendorRepository rentalVendorRepository;

    @Autowired
    private ListService listService;

    @Override
    public VehicleResponseDTO createVehicle(CreateVehicleRequestDTO dto) throws Exception {
        if (!listService.getVehicleTypeOptions().contains(dto.getType())) {
            throw new Exception("Tipe kendaraan tidak valid");
        }
        if (!listService.getTransmissionOptions().contains(dto.getTransmission())) {
            throw new Exception("Transmisi kendaraan tidak valid");
        }
        if (!listService.getFuelTypeOptions().contains(dto.getFuelType())) {
            throw new Exception("Tipe bahan bakar kendaraan tidak valid");
        }

        int currentYear = Year.now().getValue();
        if (dto.getProductionYear() > currentYear) {
            throw new Exception("Tahun keluaran kendaraan tidak valid");
        }

        if (vehicleRepository.existsById(dto.getLicencePlate())) {
            throw new Exception("Nomor plat sudah terdaftar");
        }

        Optional<RentalVendor> vendorOpt = rentalVendorRepository.findById(dto.getRentalVendorId());
        if (vendorOpt.isEmpty()) {
            throw new Exception("Vendor tidak ditemukan");
        }
        RentalVendor vendor = vendorOpt.get();

        if (!vendor.getListOfLocations().contains(dto.getLocation())) {
            throw new Exception("Lokasi tidak valid untuk vendor ini");
        }

        String newId = generateVehicleId();

        Vehicle vehicle = new Vehicle();
        vehicle.setId(newId);
        vehicle.setRentalVendor(vendor);
        vehicle.setType(dto.getType());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setProductionYear(dto.getProductionYear());
        vehicle.setLocation(dto.getLocation());
        vehicle.setLicencePlate(dto.getLicencePlate());
        vehicle.setCapacity(dto.getCapacity());
        vehicle.setTransmission(dto.getTransmission());
        vehicle.setFuelType(dto.getFuelType());
        vehicle.setPrice(dto.getPrice());
        vehicle.setStatus("AVAILABLE");
        vehicleRepository.save(vehicle);

        return mapToVehicleResponseDTO(vehicle);
    }

    private String generateVehicleId() {
        var lastVehicle = vehicleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .filter(v -> v.getId().startsWith("VEH"))
                .findFirst();

        int nextNumber = 1;
        if (lastVehicle.isPresent()) {
            String lastId = lastVehicle.get().getId();
            try {
                int lastNum = Integer.parseInt(lastId.substring(3));
                nextNumber = lastNum + 1;
            } catch (Exception ignored) {}
        }
        return String.format("VEH%04d", nextNumber);
    }

    private VehicleResponseDTO mapToVehicleResponseDTO(Vehicle vehicle) {
        VehicleResponseDTO dto = new VehicleResponseDTO();
        dto.setId(vehicle.getId());
        dto.setRentalVendorId(vehicle.getRentalVendor() != null ? vehicle.getRentalVendor().getId() : 0);
        dto.setType(vehicle.getType());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setProductionYear(vehicle.getProductionYear());
        dto.setLocation(vehicle.getLocation());
        dto.setLicencePlate(vehicle.getLicencePlate());
        dto.setCapacity(vehicle.getCapacity());
        dto.setTransmission(vehicle.getTransmission());
        dto.setFuelType(vehicle.getFuelType());
        dto.setPrice(vehicle.getPrice());
        dto.setStatus(vehicle.getStatus());
        return dto;
    }
}
