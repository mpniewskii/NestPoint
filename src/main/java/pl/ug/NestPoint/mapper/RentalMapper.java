package pl.ug.NestPoint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.ug.NestPoint.domain.Rental;
import pl.ug.NestPoint.dto.RentalDTO;

@Mapper(componentModel = "spring", uses = PaymentMapper.class)
public interface RentalMapper {
    @Mapping(source = "apartment.id", target = "apartmentId")
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "apartment.address", target = "address")
    @Mapping(source = "apartment.occupied", target = "apartmentOccupied")
    @Mapping(source = "payment.rentalFees", target = "rentalFees")
    RentalDTO toDTO(Rental rental);

    @Mapping(source = "apartmentId", target = "apartment.id")
    @Mapping(source = "tenantId", target = "tenant.id")
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "rentalFees", target = "payment.rentalFees")
    Rental toEntity(RentalDTO rentalDTO);
}