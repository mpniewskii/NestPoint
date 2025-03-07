package pl.ug.NestPoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.NestPoint.domain.Apartment;
import pl.ug.NestPoint.domain.Rental;
import pl.ug.NestPoint.domain.RentalStatus;
import pl.ug.NestPoint.dto.RentalDTO;
import pl.ug.NestPoint.dto.RentalSearchCriteria;
import pl.ug.NestPoint.mapper.RentalMapper;
import pl.ug.NestPoint.repository.ApartmentRepository;
import pl.ug.NestPoint.repository.RentalRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final ApartmentRepository apartmentRepository;
    private final RentalMapper rentalMapper;

    private static final double LUXURY_THRESHOLD = 2000.0;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id).orElse(null);
    }

    @Transactional
    public Rental createRental(RentalDTO rentalDTO) {
        // Map DTO -> Rental
        Rental rental = rentalMapper.toEntity(rentalDTO);

        // Find the apartment based on apartmentId
        Apartment apartment = apartmentRepository
                .findById(rentalDTO.getApartmentId())
                .orElseThrow(() -> new RuntimeException("Apartment not found"));

        // Check if apartment is already occupied
        if (apartment.isOccupied()) {
            throw new IllegalArgumentException("Apartment is already occupied");
        }

        // Set references from Apartment
        rental.setApartment(apartment);
        rental.setOwner(apartment.getOwner());

        // totalCost = apartment rentalPrice + rentalFees
        rental.setTotalCost(apartment.getRentalPrice() + rentalDTO.getRentalFees());

        // If status is set in DTO, parse it
        if (rentalDTO.getStatus() != null) {
            rental.setStatus(RentalStatus.valueOf(rentalDTO.getStatus()));
        }

        // Mark apartment as occupied
        apartment.setOccupied(true);
        apartmentRepository.saveAndFlush(apartment);

        return rentalRepository.saveAndFlush(rental);
    }

    @Transactional
    public Rental updateRental(Long id, RentalDTO rentalDTO) {
        Rental rental = rentalRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        // Find the apartment based on apartmentId
        Apartment apartment = apartmentRepository
                .findById(rentalDTO.getApartmentId())
                .orElseThrow(() -> new RuntimeException("Apartment not found"));

        // If user tries to switch to another occupied apartment
        if (apartment.isOccupied() && !apartment.getId().equals(rental.getApartment().getId())) {
            throw new IllegalArgumentException("Apartment is already occupied");
        }

        rental.setApartment(apartment);
        rental.setOwner(apartment.getOwner());

        // Recalculate totalCost
        rental.setTotalCost(apartment.getRentalPrice() + rentalDTO.getRentalFees());

        if (rentalDTO.getStatus() != null) {
            rental.setStatus(RentalStatus.valueOf(rentalDTO.getStatus()));
        }

        // Occupy the new apartment if changed
        apartment.setOccupied(true);
        apartmentRepository.saveAndFlush(apartment);

        return rentalRepository.saveAndFlush(rental);
    }

    @Transactional
    public void deleteRental(Long id) {
        Rental rental = rentalRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        Apartment apartment = rental.getApartment();

        apartment.setOccupied(false);
        apartmentRepository.saveAndFlush(apartment);

        rentalRepository.deleteById(id);
    }

    public Page<Rental> findByApartmentAddressContaining(String address, Pageable pageable) {
        return rentalRepository.findByApartmentAddressContaining(address, pageable);
    }

    public Page<Rental> findByTenantName(String tenantName, Pageable pageable) {
        return rentalRepository.findByTenantName(tenantName, pageable);
    }

    public Page<Rental> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return rentalRepository.findByDateRange(startDate, endDate, pageable);
    }

    public Page<Rental> findByOwnerName(String ownerName, Pageable pageable) {
        return rentalRepository.findByOwnerName(ownerName, pageable);
    }

    public Page<Rental> findByOccupied(boolean occupied, Pageable pageable) {
        return rentalRepository.findByOccupied(occupied, pageable);
    }

    public Page<Rental> searchRentals(RentalSearchCriteria criteria,
                                      int page,
                                      int size,
                                      String sortBy,
                                      String direction) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                direction.equalsIgnoreCase("ASC")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending()
        );

        if (criteria.getAddress() != null) {
            return rentalRepository.findByApartmentAddressContaining(criteria.getAddress(), pageable);
        } else if (criteria.getOccupied() != null) {
            return rentalRepository.findByOccupied(criteria.getOccupied(), pageable);
        } else if (criteria.getOwnerName() != null) {
            return rentalRepository.findByOwnerName(criteria.getOwnerName(), pageable);
        } else if (criteria.getTenantName() != null) {
            return rentalRepository.findByTenantName(criteria.getTenantName(), pageable);
        } else if (criteria.getRentalStatus() != null) {
            return rentalRepository.findByStatus(RentalStatus.valueOf(criteria.getRentalStatus()), pageable);
        }
        return rentalRepository.findAll(pageable);
    }

    public Page<Rental> findRentalsByTotalCostGreaterThan(double cost, Pageable pageable) {
        return rentalRepository.findRentalsByTotalCostGreaterThan(cost, pageable);
    }

    public Page<Rental> findLuxuryRentals(Pageable pageable) {
        return rentalRepository.findLuxuryRentals(LUXURY_THRESHOLD, pageable);
    }

    public Page<Rental> findBudgetRentals(Pageable pageable) {
        return rentalRepository.findBudgetRentals(LUXURY_THRESHOLD, pageable);
    }

    public List<Object[]> findAverageRentalCostGroupedByCity() {
        return rentalRepository.findAverageRentalCostGroupedByCity();
    }
}