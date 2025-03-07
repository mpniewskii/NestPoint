package pl.ug.NestPoint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.ug.NestPoint.domain.Owner;

import java.util.List;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Owner findByName(String name);

    @Query("SELECT o FROM Owner o JOIN o.apartments a WHERE a.occupied = :occupied")
    List<Owner> findOwnersByApartmentOccupied(@Param("occupied") boolean occupied);

    @Query("SELECT o FROM Owner o JOIN o.apartments a WHERE a.address.street LIKE %:address% OR a.address.city LIKE %:address% OR a.address.postalCode LIKE %:address%")
    List<Owner> findOwnersByApartmentAddress(@Param("address") String address);

    @Query("SELECT o FROM Owner o JOIN o.apartments a WHERE a.rentalPrice > :price")
    List<Owner> findOwnersByApartmentRentalPriceGreaterThan(@Param("price") double price);

    // Native query
    @Query(value = "SELECT * FROM Owner o WHERE o.email = :email", nativeQuery = true)
    Owner findOwnerByEmailNative(@Param("email") String email);

    @Query("SELECT o.name, (p.rentalFees / a.rentalPrice) * 100 AS feePercentage " +
           "FROM Owner o " +
           "JOIN o.apartments a " +
           "JOIN a.rentals r " +
           "JOIN r.payment p " +
           "WHERE o.id = :ownerId")
    List<Object[]> findOwnerFeePercentage(@Param("ownerId") Long ownerId);
}