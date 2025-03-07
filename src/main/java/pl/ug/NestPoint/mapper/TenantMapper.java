package pl.ug.NestPoint.mapper;

import org.mapstruct.Mapper;
import pl.ug.NestPoint.domain.Tenant;
import pl.ug.NestPoint.dto.TenantDTO;

@Mapper(componentModel = "spring")
public interface TenantMapper {
    TenantDTO toDTO(Tenant tenant);
    Tenant toEntity(TenantDTO tenantDTO);
}