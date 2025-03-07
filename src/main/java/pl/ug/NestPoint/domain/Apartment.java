package pl.ug.NestPoint.domain;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Min(value = 10, message = "Size must be at least 10 square meters")
    @Max(value = 10000, message = "Size must be at most 10000 square meters")
    private int size;

    @DecimalMin(value = "100.0", message = "Rental price must be at least 100.0")
    @DecimalMax(value = "10000.0", message = "Rental price must be at most 10000.0")
    private double rentalPrice;

    @Min(value = 1, message = "Number of rooms must be at least 1")
    @Max(value = 20, message = "Number of rooms must be at most 20")
    private int numberOfRooms;

    private boolean furnished;
    private boolean occupied;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rental> rentals;
}