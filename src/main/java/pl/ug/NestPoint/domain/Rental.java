package pl.ug.NestPoint.domain;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @AssertTrue(message = "End date must be after start date")
    private boolean isEndDateValid() {
        // Jeśli data nie jest ustawiona, walidacja przechodzi
        // Jeśli startDate nie jest ustawione, też przepuszczamy
        // W przeciwnym wypadku sprawdzamy, czy endDate jest po startDate
        return endDate == null || startDate == null || endDate.isAfter(startDate);
    }

    private double totalCost;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @OneToOne(mappedBy = "rental", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    private Double rentalFees;
}