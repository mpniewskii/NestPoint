package pl.ug.NestPoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.NestPoint.domain.Tenant;
import pl.ug.NestPoint.repository.TenantRepository;
import pl.ug.NestPoint.domain.RentalStatus;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {
    private final TenantRepository tenantRepository;

    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id).orElse(null);
    }

    @Transactional
    public Tenant createTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    @Transactional
    public Tenant updateTenant(Long id, Tenant updatedTenant) {
        Tenant tenant = tenantRepository.findById(id).orElseThrow(() -> new RuntimeException("Tenant not found"));
        tenant.setName(updatedTenant.getName());
        tenant.setSurname(updatedTenant.getSurname());
        tenant.setEmail(updatedTenant.getEmail());
        tenant.setPhone(updatedTenant.getPhone());
        tenant.setIdentifier(updatedTenant.getIdentifier());
        return tenantRepository.save(tenant);
    }

    @Transactional
    public void deleteTenant(Long id) {
        tenantRepository.deleteById(id);
    }

    public List<Tenant> findTenantsByRentalStatus(RentalStatus status) {
        return tenantRepository.findTenantsByRentalStatus(status);
    }

    public List<Tenant> findTenantsByApartmentAddress(String address) {
        return tenantRepository.findTenantsByApartmentAddress(address);
    }

    public List<Tenant> findTenantsByRentalDateRange(LocalDate startDate, LocalDate endDate) {
        return tenantRepository.findTenantsByRentalDateRange(startDate, endDate);
    }
}