package pl.ug.NestPoint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ug.NestPoint.domain.Apartment;
import pl.ug.NestPoint.domain.Owner;
import pl.ug.NestPoint.repository.OwnerRepository;
import pl.ug.NestPoint.repository.ApartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final ApartmentRepository apartmentRepository; // Dodaj to pole

    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }

    public Owner getOwnerById(Long id) {
        return ownerRepository.findById(id).orElse(null);
    }

    public Owner createOwner(Owner owner) {
        return ownerRepository.save(owner);
    }

    public Owner updateOwner(Long id, Owner updatedOwner) {
        Owner owner = ownerRepository.findById(id).orElseThrow(() -> new RuntimeException("Owner not found"));
        owner.setName(updatedOwner.getName());
        owner.setEmail(updatedOwner.getEmail());
        return ownerRepository.save(owner);
    }

    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }

    public List<Owner> findOwnersByApartmentOccupied(boolean occupied) {
        return ownerRepository.findOwnersByApartmentOccupied(occupied);
    }

    public List<Owner> findOwnersByApartmentAddress(String address) {
        return ownerRepository.findOwnersByApartmentAddress(address);
    }

    public List<Owner> findOwnersByApartmentRentalPriceGreaterThan(double price) {
        return ownerRepository.findOwnersByApartmentRentalPriceGreaterThan(price);
    }

    // Native query
    public Owner findOwnerByEmail(String email) {
        return ownerRepository.findOwnerByEmailNative(email);
    }

    // New method to get the fee percentage of an owner
    public List<Object[]> getOwnerFeePercentage(Long ownerId) {
        return ownerRepository.findOwnerFeePercentage(ownerId);
    }

    // New method to get the fee percentage of owners with apartments in a specific address
    public List<Object[]> getOwnerFeePercentageByAddress(String address) {
        List<Apartment> apartments = apartmentRepository.findByAddressContaining(address);
        List<Long> ownerIds = apartments.stream()
                                        .map(apartment -> apartment.getOwner().getId())
                                        .distinct()
                                        .collect(Collectors.toList());
        return ownerIds.stream()
                       .flatMap(ownerId -> ownerRepository.findOwnerFeePercentage(ownerId).stream())
                       .collect(Collectors.toList());
    }
}