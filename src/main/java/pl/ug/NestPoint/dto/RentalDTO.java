package pl.ug.NestPoint.dto;

import lombok.Data;
import java.time.LocalDate;
import pl.ug.NestPoint.domain.Address;

@Data
public class RentalDTO {
    private Long id;
    private Long apartmentId;
    private Long tenantId;
    private Long ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private String status;
    private Address address;
    private boolean apartmentOccupied;
    private double rentalFees;
}