package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ug.NestPoint.domain.Tenant;
import pl.ug.NestPoint.dto.TenantDTO;
import pl.ug.NestPoint.mapper.TenantMapper;
import pl.ug.NestPoint.service.TenantService;
import pl.ug.NestPoint.domain.RentalStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {
    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    @GetMapping
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        List<TenantDTO> tenants = tenantService.getAllTenants().stream()
                .map(tenantMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tenants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getTenantById(@PathVariable Long id) {
        Tenant tenant = tenantService.getTenantById(id);
        if (tenant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tenantMapper.toDTO(tenant));
    }

    @PostMapping
    public ResponseEntity<TenantDTO> createTenant(@RequestBody TenantDTO tenantDTO) {
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenantService.createTenant(tenant);
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantMapper.toDTO(tenant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantDTO> updateTenant(@PathVariable Long id, @RequestBody TenantDTO tenantDTO) {
        Tenant updatedTenant = tenantMapper.toEntity(tenantDTO);
        tenantService.updateTenant(id, updatedTenant);
        return ResponseEntity.ok(tenantMapper.toDTO(updatedTenant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TenantDTO>> searchTenants(@RequestParam(required = false) RentalStatus status,
                                                         @RequestParam(required = false) String address,
                                                         @RequestParam(required = false) LocalDate startDate,
                                                         @RequestParam(required = false) LocalDate endDate) {
        List<TenantDTO> tenants;
        if (status != null) {
            tenants = tenantService.findTenantsByRentalStatus(status).stream()
                    .map(tenantMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (address != null) {
            tenants = tenantService.findTenantsByApartmentAddress(address).stream()
                    .map(tenantMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (startDate != null && endDate != null) {
            tenants = tenantService.findTenantsByRentalDateRange(startDate, endDate).stream()
                    .map(tenantMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            tenants = tenantService.getAllTenants().stream()
                    .map(tenantMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(tenants);
    }
}