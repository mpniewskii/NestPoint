package pl.ug.NestPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.ug.NestPoint.domain.*;
import pl.ug.NestPoint.repository.*;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class NestPointApplication implements CommandLineRunner {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public static void main(String[] args) {
        SpringApplication.run(NestPointApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}