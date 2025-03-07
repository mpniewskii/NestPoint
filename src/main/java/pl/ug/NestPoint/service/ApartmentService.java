package pl.ug.NestPoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ug.NestPoint.domain.Apartment;
import pl.ug.NestPoint.domain.Owner;
import pl.ug.NestPoint.repository.ApartmentRepository;
import pl.ug.NestPoint.repository.OwnerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final OwnerRepository ownerRepository;

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public Apartment getApartmentById(Long id) {
        return apartmentRepository.findById(id).orElse(null);
    }

    public Apartment createApartment(Apartment apartment) {
        if (apartment.getOwner() != null && apartment.getOwner().getId() != null) {
            Owner owner = ownerRepository.findById(apartment.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            apartment.setOwner(owner);
        } else {
            throw new RuntimeException("Owner must be provided");
        }
        return apartmentRepository.save(apartment);
    }

    public void deleteApartment(Long id) {
        apartmentRepository.deleteById(id);
    }

    public Apartment updateApartment(Long id, Apartment updatedApartment) {
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apartment not found"));
        if (updatedApartment.getOwner() != null && updatedApartment.getOwner().getId() != null) {
            Owner owner = ownerRepository.findById(updatedApartment.getOwner().getId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            apartment.setOwner(owner);
        } else {
            throw new RuntimeException("Owner must be provided");
        }
        apartment.setFurnished(updatedApartment.isFurnished());
        apartment.setRentalPrice(updatedApartment.getRentalPrice());
        apartment.setNumberOfRooms(updatedApartment.getNumberOfRooms());
        return apartmentRepository.save(apartment);
    }

    public List<Apartment> findByOccupied(boolean occupied) {
        return apartmentRepository.findByOccupied(occupied);
    }

    public List<Apartment> findByOwnerName(String ownerName) {
        return apartmentRepository.findByOwnerName(ownerName);
    }

    public List<Apartment> findByAddressContaining(String address) {
        return apartmentRepository.findByAddressContaining(address);
    }

    public List<Apartment> findBySizeGreaterThan(int size) {
        return apartmentRepository.findBySizeGreaterThan(size);
    }

    public List<Apartment> findByRentalPriceBetween(double minPrice, double maxPrice) {
        return apartmentRepository.findByRentalPriceBetween(minPrice, maxPrice);
    }

    public List<Apartment> findLuxuryApartments(double luxuryThreshold) {
        return apartmentRepository.findLuxuryApartments(luxuryThreshold);
    }

    public List<Apartment> findBudgetApartments(double luxuryThreshold) {
        return apartmentRepository.findBudgetApartments(luxuryThreshold);
    }

    public List<Object[]> findAverageRentalPriceGroupedByCity() {
        return apartmentRepository.findAverageRentalPriceGroupedByCity();
    }

    public List<Object[]> findAverageRentalPriceGroupedByOwner() {
        return apartmentRepository.findAverageRentalPriceGroupedByOwner();
    }
}