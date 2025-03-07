package pl.ug.NestPoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ug.NestPoint.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}