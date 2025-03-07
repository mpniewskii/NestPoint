package pl.ug.NestPoint.mapper;

import org.mapstruct.Mapper;
import pl.ug.NestPoint.domain.Owner;
import pl.ug.NestPoint.dto.OwnerDTO;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    OwnerDTO toDTO(Owner owner);
    Owner toEntity(OwnerDTO ownerDTO);
}