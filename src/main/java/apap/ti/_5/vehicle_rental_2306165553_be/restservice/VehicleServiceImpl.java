package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.model.Vehicle;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalVendor;
import apap.ti._5.vehicle_rental_2306165553_be.repository.VehicleRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalVendorRepository;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.CreateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.vehicle.UpdateVehicleRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.vehicle.VehicleResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.Year;
import java.util.*;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private RentalVendorRepository rentalVendorRepository;

    @Autowired
    private ListService listService;

    @Override
    public List<VehicleResponseDTO> getAllVehicle(String search, String filterByType) {
        List<VehicleResponseDTO> vehicles = vehicleRepository.findAll()
            .stream()
            .filter(v -> v.getDeletedAt() == null)
            .map(this::mapToVehicleResponseDTO)
            .toList();

        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase();
            vehicles = vehicles.stream()
                .filter(v ->
                    (v.getId() != null && v.getId().toLowerCase().contains(searchLower)) ||
                    (v.getBrand() != null && v.getBrand().toLowerCase().contains(searchLower)) ||
                    (v.getModel() != null && v.getModel().toLowerCase().contains(searchLower)) ||
                    (v.getLicencePlate() != null && v.getLicencePlate().toLowerCase().contains(searchLower)) ||
                    (v.getType() != null && v.getType().toLowerCase().contains(searchLower)) ||
                    (v.getLocation() != null && v.getLocation().toLowerCase().contains(searchLower)) ||
                    (v.getTransmission() != null && v.getTransmission().toLowerCase().contains(searchLower)) ||
                    (v.getFuelType() != null && v.getFuelType().toLowerCase().contains(searchLower)) ||
                    (v.getStatus() != null && v.getStatus().toLowerCase().contains(searchLower)) ||
                    (String.valueOf(v.getCapacity()).contains(searchLower)) ||
                    (String.valueOf(v.getRentalVendorId()).contains(searchLower)) ||
                    (String.valueOf(v.getPrice()).contains(searchLower)) ||
                    (v.getRentalVendorName() != null && v.getRentalVendorName().toLowerCase().contains(searchLower))
                )
                .toList();
        }

        if (filterByType != null && !filterByType.trim().isEmpty()) {
            vehicles = vehicles.stream()
                .filter(v -> v.getType().equalsIgnoreCase(filterByType))
                .toList();
        }

        return vehicles;
    }

    @Override
    public VehicleResponseDTO getVehicleById(String id) throws Exception {
        return vehicleRepository.findById(id)
            .filter(v -> v.getDeletedAt() == null)
            .map(this::mapToVehicleResponseDTO)
            .orElseThrow(() -> new Exception("Vehicle not found"));
    }

    private Optional<RentalVendor> findVendorByName(String name) {
        return rentalVendorRepository.findAll()
            .stream()
            .filter(v -> v.getName().equalsIgnoreCase(name))
            .findFirst();
    }

    @Override
    public VehicleResponseDTO createVehicle(CreateVehicleRequestDTO dto) throws Exception {
        List<String> errors = new java.util.ArrayList<>();

        if (!listService.getVehicleTypeOptions().contains(dto.getType())) {
            errors.add("Tipe kendaraan tidak valid");
        }
        if (!listService.getTransmissionOptions().contains(dto.getTransmission())) {
            errors.add("Transmisi kendaraan tidak valid");
        }
        if (!listService.getFuelTypeOptions().contains(dto.getFuelType())) {
            errors.add("Tipe bahan bakar kendaraan tidak valid");
        }

        int currentYear = Year.now().getValue();
        if (dto.getProductionYear() > currentYear) {
            errors.add("Tahun keluaran kendaraan tidak valid");
        }

        if (vehicleRepository.existsByLicencePlate(dto.getLicencePlate())) {
            errors.add("Nomor plat sudah terdaftar");
        }

        Optional<RentalVendor> vendorOpt = findVendorByName(dto.getRentalVendorName());
        if (vendorOpt.isEmpty()) {
            errors.add("Vendor tidak ditemukan");
        }

        RentalVendor vendor = vendorOpt.orElse(null);
        if (vendor != null && !vendor.getListOfLocations().contains(dto.getLocation())) {
            errors.add("Lokasi tidak valid untuk vendor ini");
        }

        if (!errors.isEmpty()) {
            throw new Exception(String.join("; ", errors));
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
        vehicle.setStatus("Available");
        vehicleRepository.save(vehicle);

        return mapToVehicleResponseDTO(vehicle);
    }

    @Override
    public VehicleResponseDTO updateVehicle(String id, UpdateVehicleRequestDTO dto) throws Exception {
        Vehicle vehicle = vehicleRepository.findById(id)
                .filter(v -> v.getDeletedAt() == null)
                .orElseThrow(() -> new Exception("Vehicle not found"));
                
        List<String> errors = new java.util.ArrayList<>();

        if ("In Use".equalsIgnoreCase(vehicle.getStatus())) {
            errors.add("Kendaraan sedang disewa, perubahan tidak diizinkan");
        }

        if (!listService.getVehicleStatusOptions().contains(dto.getStatus())) {
            errors.add("Status kendaraan tidak valid");
        }

        if (!listService.getVehicleTypeOptions().contains(dto.getType())) {
            errors.add("Tipe kendaraan tidak valid");
        }
        if (!listService.getTransmissionOptions().contains(dto.getTransmission())) {
            errors.add("Transmisi kendaraan tidak valid");
        }
        if (!listService.getFuelTypeOptions().contains(dto.getFuelType())) {
            errors.add("Tipe bahan bakar kendaraan tidak valid");
        }

        int currentYear = Year.now().getValue();
        if (dto.getProductionYear() > currentYear) {
            errors.add("Tahun keluaran kendaraan tidak valid");
        }

        Optional<RentalVendor> vendorOpt = findVendorByName(dto.getRentalVendorName());
        if (vendorOpt.isEmpty()) {
            errors.add("Vendor tidak ditemukan");
        }

        RentalVendor vendor = vendorOpt.orElse(null);
        if (vendor != null && !vendor.getListOfLocations().contains(dto.getLocation())) {
            errors.add("Lokasi tidak valid untuk vendor ini");
        }

        if (!errors.isEmpty()) {
            throw new Exception(String.join("; ", errors));
        }

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
        vehicle.setStatus(dto.getStatus());
        vehicleRepository.save(vehicle);

        return mapToVehicleResponseDTO(vehicle);
    }

    @Override
    public void deleteVehicle(String id) throws Exception {
        Vehicle vehicle = vehicleRepository.findById(id)
                .filter(v -> v.getDeletedAt() == null)
                .orElseThrow(() -> new Exception("Vehicle not found"));

        if ("In Use".equalsIgnoreCase(vehicle.getStatus())) {
            throw new Exception("Kendaraan sedang disewa, penghapusan tidak diizinkan");
        }

        vehicle.setDeletedAt(new Date());
        vehicle.setStatus("Unavailable");
        vehicleRepository.save(vehicle);
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
        dto.setRentalVendorName(vehicle.getRentalVendor() != null ? vehicle.getRentalVendor().getName() : null);
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
