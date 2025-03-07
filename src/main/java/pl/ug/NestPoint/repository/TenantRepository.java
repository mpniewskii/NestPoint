package pl.ug.NestPoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.ug.NestPoint.domain.Tenant;
import pl.ug.NestPoint.domain.RentalStatus;

import java.util.List;
import java.time.LocalDate;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Tenant findByIdentifier(String identifier);

    @Query("SELECT t FROM Tenant t JOIN t.rentals r WHERE r.status = :status")
    List<Tenant> findTenantsByRentalStatus(@Param("status") RentalStatus status);

    @Query("SELECT t FROM Tenant t " +
           "JOIN t.rentals r " +
           "JOIN r.apartment a " +
           "JOIN a.address ad " +
           "WHERE ad.street LIKE %:address% " +
           "OR ad.city LIKE %:address% " +
           "OR ad.apartmentNumber LIKE %:address% " +
           "OR ad.postalCode LIKE %:address%")
    List<Tenant> findTenantsByApartmentAddress(@Param("address") String address);

    @Query("SELECT t FROM Tenant t JOIN t.rentals r WHERE r.startDate >= :startDate AND r.endDate <= :endDate")
    List<Tenant> findTenantsByRentalDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}