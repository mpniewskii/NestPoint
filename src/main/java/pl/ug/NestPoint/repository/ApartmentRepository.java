package pl.ug.NestPoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.ug.NestPoint.domain.Apartment;

import java.util.List;

public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    List<Apartment> findByOccupied(boolean occupied);

    @Query("SELECT a FROM Apartment a JOIN a.owner o WHERE o.name = :ownerName")
    List<Apartment> findByOwnerName(@Param("ownerName") String ownerName);

    @Query("SELECT a FROM Apartment a WHERE a.address.street LIKE %:address% OR a.address.city LIKE %:address% OR a.address.postalCode LIKE %:address%")
    List<Apartment> findByAddressContaining(@Param("address") String address);

    @Query(value = "SELECT * FROM apartment a WHERE a.size > :size", nativeQuery = true)
    List<Apartment> findBySizeGreaterThan(@Param("size") int size);

    @Query("SELECT a FROM Apartment a WHERE a.rentalPrice BETWEEN :minPrice AND :maxPrice")
    List<Apartment> findByRentalPriceBetween(@Param("minPrice") double minPrice, @Param("maxPrice") double maxPrice);

    @Query("SELECT a FROM Apartment a WHERE a.rentalPrice > :luxuryThreshold")
    List<Apartment> findLuxuryApartments(@Param("luxuryThreshold") double luxuryThreshold);

    @Query("SELECT a FROM Apartment a WHERE a.rentalPrice < :luxuryThreshold")
    List<Apartment> findBudgetApartments(@Param("luxuryThreshold") double luxuryThreshold);

    @Query("SELECT a.address.city, AVG(a.rentalPrice) FROM Apartment a GROUP BY a.address.city")
    List<Object[]> findAverageRentalPriceGroupedByCity();

    @Query("SELECT a.owner.name, AVG(a.rentalPrice) FROM Apartment a GROUP BY a.owner.name")
    List<Object[]> findAverageRentalPriceGroupedByOwner();
}