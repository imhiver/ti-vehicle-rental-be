package apap.ti._5.vehicle_rental_2306165553_be.restservice;

import apap.ti._5.vehicle_rental_2306165553_be.model.RentalBooking;
import apap.ti._5.vehicle_rental_2306165553_be.model.Vehicle;
import apap.ti._5.vehicle_rental_2306165553_be.model.RentalAddOn;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalBookingRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.VehicleRepository;
import apap.ti._5.vehicle_rental_2306165553_be.repository.RentalAddOnRepository;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.CreateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateRentalBookingRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingStatusRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.request.rentalbooking.UpdateBookingAddOnsRequestDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.RentalBookingResponseDTO;
import apap.ti._5.vehicle_rental_2306165553_be.restdto.response.rentalbooking.BookingChartResponseDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RentalBookingServiceImpl implements RentalBookingService {
    private final RentalBookingRepository rentalBookingRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalAddOnRepository rentalAddOnRepository;

    public RentalBookingServiceImpl(
        RentalBookingRepository rentalBookingRepository,
        VehicleRepository vehicleRepository,
        RentalAddOnRepository rentalAddOnRepository
    ) {
        this.rentalBookingRepository = rentalBookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.rentalAddOnRepository = rentalAddOnRepository;
    }

    @Override
    public List<RentalBookingResponseDTO> getAllBookings(String search) {
        List<RentalBooking> bookings = rentalBookingRepository.findAll()
            .stream()
            .filter(b -> b.getDeletedAt() == null)
            .collect(Collectors.toList());

        if (search != null && !search.isEmpty()) {
            String keyword = search.toLowerCase();
            bookings = bookings.stream()
                .filter(b ->
                    (b.getId() != null && b.getId().toLowerCase().contains(keyword)) ||
                    (b.getPickUpLocation() != null && b.getPickUpLocation().toLowerCase().contains(keyword)) ||
                    (b.getDropOffLocation() != null && b.getDropOffLocation().toLowerCase().contains(keyword)) ||
                    (b.getStatus() != null && b.getStatus().toLowerCase().contains(keyword)) ||
                    (b.getVehicle() != null && b.getVehicle().getId() != null && b.getVehicle().getId().toLowerCase().contains(keyword)) ||
                    (String.valueOf(b.getTotalPrice()).contains(keyword))
                )
                .collect(Collectors.toList());
        }

        return bookings.stream()
            .map(this::mapToRentalBookingResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public RentalBookingResponseDTO getBookingById(String id) throws Exception {
        RentalBooking booking = rentalBookingRepository.findById(id)
            .filter(b -> b.getDeletedAt() == null)
            .orElseThrow(() -> new Exception("Booking not found"));
        return mapToRentalBookingResponseDTO(booking);
    }


    @Override
    public RentalBookingResponseDTO createBooking(CreateRentalBookingRequestDTO dto) throws Exception {
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
            .filter(v -> v.getDeletedAt() == null)
            .orElseThrow(() -> new Exception("Vehicle not found"));

        if (vehicle.getRentalVendor() == null) {
            throw new Exception("Rental vendor not found for vehicle");
        }
        List<String> locations = vehicle.getRentalVendor().getListOfLocations();
        if (!locations.contains(dto.getPickUpLocation()) || !locations.contains(dto.getDropOffLocation())) {
            throw new Exception("Pick-up or drop-off location is not valid for this vendor");
        }

        double total = dto.getBaseTotalPrice();

        List<RentalAddOn> selectedAddOns = new ArrayList<>();
        if (dto.getAddOns() != null && !dto.getAddOns().isEmpty()) {
            for (String addOnName : dto.getAddOns()) {
                RentalAddOn addOn = rentalAddOnRepository.findByName(addOnName)
                    .orElseThrow(() -> new Exception("Add-on tidak ditemukan: " + addOnName));
                selectedAddOns.add(addOn);
                total += addOn.getPrice();
            }
        }

        RentalBooking booking = new RentalBooking();
        booking.setId(generateBookingId());
        booking.setVehicle(vehicle);
        booking.setPickUpTime(dto.getPickUpTime());
        booking.setDropOffTime(dto.getDropOffTime());
        booking.setPickUpLocation(dto.getPickUpLocation());
        booking.setDropOffLocation(dto.getDropOffLocation());
        booking.setIncludeDriver(dto.isIncludeDriver());
        booking.setTransmissionNeeded(dto.getTransmissionNeeded());
        String status = determineBookingStatus(booking.getPickUpTime(), booking.getDropOffTime());
        if (status.equals("Ongoing")) {
            List<RentalBooking> ongoingBookings = rentalBookingRepository.findByVehicle_IdAndStatusIn(
                vehicle.getId(), List.of("Ongoing")
            );
            if (!ongoingBookings.isEmpty()) {
                throw new Exception("Kendaraan masih digunakan oleh pesanan lain yang Ongoing. Tunggu hingga pesanan sebelumnya selesai.");
            }
            vehicle.setStatus("In Use");
            
        }
        booking.setStatus(status);
        booking.setTotalPrice(total);
        booking.setListOfAddOns(selectedAddOns);
        booking.setCapacityNeeded(dto.getCapacityNeeded());
        

        rentalBookingRepository.save(booking);

        return mapToRentalBookingResponseDTO(booking);
    }

    @Override
    public RentalBookingResponseDTO updateBooking(String id, UpdateRentalBookingRequestDTO dto) throws Exception {
        RentalBooking booking = rentalBookingRepository.findById(id)
            .filter(b -> b.getDeletedAt() == null)
            .orElseThrow(() -> new Exception("Booking not found"));

        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
            .filter(v -> v.getDeletedAt() == null)
            .orElseThrow(() -> new Exception("Vehicle not found"));

        if (vehicle.getRentalVendor() == null) {
            throw new Exception("Rental vendor not found for vehicle");
        }
        List<String> locations = vehicle.getRentalVendor().getListOfLocations();
        if (!locations.contains(dto.getPickUpLocation()) || !locations.contains(dto.getDropOffLocation())) {
            throw new Exception("Pick-up or drop-off location is not valid for this vendor");
        }

        List<RentalAddOn> selectedAddOns = new ArrayList<>();
        
        double total = dto.getBaseTotalPrice();
        if (booking.getListOfAddOns() != null) {
            for (RentalAddOn addOn : booking.getListOfAddOns()) {
                selectedAddOns.add(addOn);
                total += addOn.getPrice();
            }
        }
        
        booking.setTotalPrice(total);
        booking.setVehicle(vehicle);
        booking.setPickUpTime(dto.getPickUpTime());
        booking.setDropOffTime(dto.getDropOffTime());
        booking.setPickUpLocation(dto.getPickUpLocation());
        booking.setDropOffLocation(dto.getDropOffLocation());
        booking.setIncludeDriver(dto.isIncludeDriver());
        booking.setTransmissionNeeded(dto.getTransmissionNeeded());
        booking.setCapacityNeeded(dto.getCapacityNeeded());
        booking.setStatus(booking.getStatus());
        booking.setListOfAddOns(selectedAddOns);
        rentalBookingRepository.save(booking);

        return mapToRentalBookingResponseDTO(booking);
    }

    @Override
    public RentalBookingResponseDTO updateBookingStatus(UpdateBookingStatusRequestDTO dto) throws Exception {
        RentalBooking booking = rentalBookingRepository.findById(dto.getBookingId())
            .filter(b -> b.getDeletedAt() == null)
            .orElseThrow(() -> new Exception("Booking not found"));

        String currentStatus = booking.getStatus();
        String newStatus = dto.getNewStatus();

        Date now = new Date();
        Vehicle vehicle = booking.getVehicle();

        if (currentStatus.equals("Upcoming")) {
            if (newStatus.equals("Ongoing")) {
                List<RentalBooking> ongoingBookings = rentalBookingRepository.findByVehicle_IdAndStatusIn(
                    vehicle.getId(), List.of("Ongoing")
                );
                for (RentalBooking other : ongoingBookings) {
                    if (!other.getId().equals(booking.getId()) &&
                        booking.getPickUpTime().before(other.getDropOffTime()) &&
                        booking.getDropOffTime().after(other.getPickUpTime())) {
                        throw new Exception("Kendaraan masih digunakan oleh pesanan lain yang Ongoing. Tunggu hingga pesanan sebelumnya selesai.");
                    }
                }
                if (now.before(booking.getPickUpTime()))
                    throw new Exception("Belum memasuki waktu pengambilan kendaraan");
                if (now.after(booking.getDropOffTime()))
                    throw new Exception("Sudah melewati waktu pengembalian kendaraan");
                if (!vehicle.getStatus().equals("Available"))
                    throw new Exception("Kendaraan tidak tersedia");
                if (!vehicle.getLocation().equals(booking.getPickUpLocation()))
                    throw new Exception("Kendaraan tidak ada di lokasi pengambilan");

                booking.setStatus("Ongoing");
                vehicle.setStatus("In Use");
                vehicle.setLocation(booking.getPickUpLocation());
                vehicleRepository.save(vehicle);
            } else if (newStatus.equals("Done")) {
                throw new Exception("Tidak bisa mengubah Upcoming menjadi Done, hanya bisa dibatalkan");
            }
        } else if (currentStatus.equals("Ongoing")) {
            if (newStatus.equals("Done")) {
                vehicle.setStatus("Available");
                vehicle.setLocation(booking.getDropOffLocation());
                vehicleRepository.save(vehicle);

                long lateMillis = now.getTime() - booking.getDropOffTime().getTime();
                double penalty = 0;
                if (lateMillis > 0) {
                    long lateHours = (lateMillis + 3599999) / 3600000; 
                    penalty = lateHours * 20000;
                    booking.setTotalPrice(booking.getTotalPrice() + penalty);
                }
                booking.setStatus("Done");
            }
        } else if (currentStatus.equals("Done")) {
            throw new Exception("Status sudah Done, tidak bisa diubah lagi");
        }

        rentalBookingRepository.save(booking);
        return mapToRentalBookingResponseDTO(booking);
    }

    @Override
    public RentalBookingResponseDTO updateBookingAddOns(UpdateBookingAddOnsRequestDTO dto) throws Exception {
        RentalBooking booking = rentalBookingRepository.findById(dto.getBookingId())
            .filter(b -> b.getDeletedAt() == null)
            .orElseThrow(() -> new Exception("Booking not found"));

        if (!booking.getStatus().equals("Upcoming")) {
            throw new Exception("Add-ons hanya bisa diubah jika status booking Upcoming");
        }

        double total = 0;
        long diffMillis =  booking.getDropOffTime().getTime() - booking.getPickUpTime().getTime();
        final int days = Math.max(1, (int) Math.ceil(diffMillis / (1000.0 * 60 * 60 * 24)));
        if (booking.isIncludeDriver()) total += days * 100_000;
        if (booking.getVehicle() != null) {
            total += days * booking.getVehicle().getPrice();
        }
        
        List<RentalAddOn> selectedAddOns = new ArrayList<>();
        if (dto.getAddOns() != null && !dto.getAddOns().isEmpty()) {
            for (String addOnName : dto.getAddOns()) {
                RentalAddOn addOn = rentalAddOnRepository.findByName(addOnName)
                    .orElseThrow(() -> new Exception("Add-on tidak ditemukan: " + addOnName));
                selectedAddOns.add(addOn);
                total += addOn.getPrice();
            }
        }

        booking.setListOfAddOns(selectedAddOns);
        booking.setTotalPrice(total);

        rentalBookingRepository.save(booking);

        return mapToRentalBookingResponseDTO(booking);
    }

    @Override
    public void cancelBooking(String id) throws Exception {
        RentalBooking booking = rentalBookingRepository.findById(id)
            .filter(b -> b.getDeletedAt() == null)
            .orElseThrow(() -> new Exception("Booking not found"));

        Date now = new Date();
        Vehicle vehicle = booking.getVehicle();

        if (!booking.getStatus().equals("Upcoming")) {
            throw new Exception("Penghapusan hanya bisa dilakukan ketika pesanan masih berstatus Upcoming");
        }

        if (now.before(booking.getPickUpTime())) {
            booking.setTotalPrice(0);
        }

        booking.setStatus("Done");
        booking.setDeletedAt(now);

        if (vehicle != null) {
            vehicle.setStatus("Available");
            vehicle.setLocation(booking.getDropOffLocation());
            vehicleRepository.save(vehicle);
        }

        rentalBookingRepository.save(booking);
    }

    @Override
    public List<BookingChartResponseDTO> getBookingChart(String period, int year) {
        List<RentalBooking> bookings = rentalBookingRepository.findAll()
            .stream()
            .filter(b -> b.getDeletedAt() == null && b.getCreatedAt() != null)
            .filter(b -> {
                Calendar cal = Calendar.getInstance();
                cal.setTime(b.getCreatedAt());
                return cal.get(Calendar.YEAR) == year;
            })
            .toList();

        List<BookingChartResponseDTO> chart = new ArrayList<>();
        if ("monthly".equalsIgnoreCase(period)) {
            int[] monthlyCount = new int[12];
            for (RentalBooking b : bookings) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(b.getCreatedAt());
                int month = cal.get(Calendar.MONTH); 
                monthlyCount[month]++;
            }
            String[] monthLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            for (int i = 0; i < 12; i++) {
                BookingChartResponseDTO dto = new BookingChartResponseDTO();
                dto.setLabel(monthLabels[i]);
                dto.setTotalBookings(monthlyCount[i]);
                chart.add(dto);
            }
        } else if ("quarterly".equalsIgnoreCase(period)) {
            int[] quarterlyCount = new int[4];
            for (RentalBooking b : bookings) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(b.getCreatedAt());
                int quarter = cal.get(Calendar.MONTH) / 3;
                quarterlyCount[quarter]++;
            }
            String[] quarterLabels = {"Q1", "Q2", "Q3", "Q4"};
            for (int i = 0; i < 4; i++) {
                BookingChartResponseDTO dto = new BookingChartResponseDTO();
                dto.setLabel(quarterLabels[i]);
                dto.setTotalBookings(quarterlyCount[i]);
                chart.add(dto);
            }
        }
        return chart;
    }

    private String generateBookingId() {
        var last = rentalBookingRepository.findAll()
                .stream()
                .filter(b -> b.getId().startsWith("VR"))
                .max(Comparator.comparing(RentalBooking::getId));
        int next = 1;
        if (last.isPresent()) {
            String lastId = last.get().getId();
            try {
                int num = Integer.parseInt(lastId.substring(4));
                next = num + 1;
            } catch (Exception ignored) {}
        }
        return String.format("VR%04d", next);
    }

    private RentalBookingResponseDTO mapToRentalBookingResponseDTO(RentalBooking booking) {
        RentalBookingResponseDTO dto = new RentalBookingResponseDTO();
        dto.setId(booking.getId());
        dto.setVehicleId(booking.getVehicle() != null ? booking.getVehicle().getId() : null);
        dto.setPickUpTime(booking.getPickUpTime());
        dto.setDropOffTime(booking.getDropOffTime());
        dto.setPickUpLocation(booking.getPickUpLocation());
        dto.setDropOffLocation(booking.getDropOffLocation());
        dto.setIncludeDriver(booking.isIncludeDriver());
        dto.setTransmissionNeeded(booking.getTransmissionNeeded());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        List<String> addOnNames = booking.getListOfAddOns() != null
            ? booking.getListOfAddOns().stream().map(RentalAddOn::getName).toList()
            : new ArrayList<>();
        dto.setAddOnNames(addOnNames);
        dto.setCapacityNeeded(booking.getCapacityNeeded());
        return dto;
    }

    private String determineBookingStatus(Date pickUpTime, Date dropOffTime) {
        Date now = new Date();
        if (now.before(pickUpTime)) {
            return "Upcoming";
        } else if (now.after(dropOffTime) || now.equals(dropOffTime)) {
            return "Done";
        } else {
            return "Ongoing";
        }
    }
}
