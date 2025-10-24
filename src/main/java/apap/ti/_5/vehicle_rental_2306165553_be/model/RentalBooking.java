package apap.ti._5.vehicle_rental_2306165553_be.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rental_booking")
public class RentalBooking {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "pick_up_time", nullable = false)
    private Date pickUpTime;

    @Column(name = "drop_off_time", nullable = false)
    private Date dropOffTime;

    @Column(name = "pick_up_location", nullable = false)
    private String pickUpLocation;

    @Column(name = "drop_off_location", nullable = false)
    private String dropOffLocation;

    @Column(name = "capacity_needed", nullable = false)
    private int capacityNeeded;

    @Column(name = "transmission_needed", nullable = false)
    private String transmissionNeeded;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    @Column(name = "include_driver", nullable = false)
    private boolean includeDriver;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToMany
    @JoinTable(
        name = "rental_booking_addon",
        joinColumns = @JoinColumn(name = "rental_booking_id"),
        inverseJoinColumns = @JoinColumn(name = "rental_addon_id")
    )
    private List<RentalAddOn> listOfAddOns = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
