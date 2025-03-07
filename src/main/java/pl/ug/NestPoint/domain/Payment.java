package pl.ug.NestPoint.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private LocalDate paymentDate;
    private double rentalFees = 0.0; // Wartosc poczatkowa

    @OneToOne
    @JoinColumn(name = "rental_id")
    private Rental rental;
}