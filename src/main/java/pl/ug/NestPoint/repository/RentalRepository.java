package pl.ug.NestPoint.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.ug.NestPoint.domain.Rental;
import pl.ug.NestPoint.domain.RentalStatus;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Page<Rental> findByStatus(RentalStatus status, Pageable pageable);

    @Query("SELECT r FROM Rental r JOIN r.apartment a WHERE a.address.street LIKE %:address% OR a.address.city LIKE %:address% OR a.address.postalCode LIKE %:address%")
    Page<Rental> findByApartmentAddressContaining(@Param("address") String address, Pageable pageable);

    @Query("SELECT r FROM Rental r JOIN r.tenant t WHERE t.name = :tenantName")
    Page<Rental> findByTenantName(@Param("tenantName") String tenantName, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.startDate >= :startDate AND r.endDate <= :endDate")
    Page<Rental> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query("SELECT r FROM Rental r JOIN r.apartment a JOIN a.owner o WHERE o.name = :ownerName")
    Page<Rental> findByOwnerName(@Param("ownerName") String ownerName, Pageable pageable);

    @Query("SELECT r FROM Rental r JOIN r.apartment a WHERE a.occupied = :occupied")
    Page<Rental> findByOccupied(@Param("occupied") boolean occupied, Pageable pageable);

    // Native query
    @Query(value = "SELECT * FROM Rental r WHERE r.total_cost > :cost", nativeQuery = true)
    Page<Rental> findRentalsByTotalCostGreaterThan(@Param("cost") double cost, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.totalCost > :luxuryThreshold")
    Page<Rental> findLuxuryRentals(@Param("luxuryThreshold") double luxuryThreshold, Pageable pageable);

    @Query("SELECT r FROM Rental r WHERE r.totalCost < :luxuryThreshold")
    Page<Rental> findBudgetRentals(@Param("luxuryThreshold") double luxuryThreshold, Pageable pageable);

    @Query("SELECT r.apartment.address.city, AVG(r.totalCost) FROM Rental r GROUP BY r.apartment.address.city")
    List<Object[]> findAverageRentalCostGroupedByCity();
}