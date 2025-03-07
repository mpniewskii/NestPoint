package pl.ug.NestPoint.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.ug.NestPoint.domain.Apartment;
import pl.ug.NestPoint.dto.ApartmentDTO;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    ApartmentDTO toDTO(Apartment apartment);

    @Mapping(source = "ownerId", target = "owner.id")
    Apartment toEntity(ApartmentDTO apartmentDTO);
}