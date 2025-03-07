package pl.ug.NestPoint.dto;

import lombok.Data;
import pl.ug.NestPoint.domain.Address;

@Data
public class ApartmentDTO {
    private Long id;
    private Address address;
    private int size;
    private double rentalPrice;
    private int numberOfRooms;
    private boolean furnished;
    private boolean occupied;
    private Long ownerId;
}