package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ug.NestPoint.domain.Apartment;
import pl.ug.NestPoint.dto.ApartmentDTO;
import pl.ug.NestPoint.mapper.ApartmentMapper;
import pl.ug.NestPoint.service.ApartmentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentController {
    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;

    @GetMapping
    public ResponseEntity<List<ApartmentDTO>> getAllApartments() {
        List<ApartmentDTO> apartments = apartmentService.getAllApartments().stream()
                .map(apartmentMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentDTO> getApartmentById(@PathVariable Long id) {
        Apartment apartment = apartmentService.getApartmentById(id);
        if (apartment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(apartmentMapper.toDTO(apartment));
    }

    @PostMapping
    public ResponseEntity<ApartmentDTO> createApartment(@RequestBody ApartmentDTO apartmentDTO) {
        Apartment apartment = apartmentMapper.toEntity(apartmentDTO);
        apartmentService.createApartment(apartment);
        return ResponseEntity.status(HttpStatus.CREATED).body(apartmentMapper.toDTO(apartment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentDTO> updateApartment(@PathVariable Long id, @RequestBody ApartmentDTO apartmentDTO) {
        Apartment updatedApartment = apartmentMapper.toEntity(apartmentDTO);
        apartmentService.updateApartment(id, updatedApartment);
        return ResponseEntity.ok(apartmentMapper.toDTO(updatedApartment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ApartmentDTO>> searchApartments(@RequestParam(required = false) Boolean occupied,
                                                               @RequestParam(required = false) String ownerName,
                                                               @RequestParam(required = false) String address,
                                                               @RequestParam(required = false) Integer size,
                                                               @RequestParam(required = false) Double minPrice,
                                                               @RequestParam(required = false) Double maxPrice) {
        List<ApartmentDTO> apartments;
        if (occupied != null) {
            apartments = apartmentService.findByOccupied(occupied).stream()
                    .map(apartmentMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (ownerName != null) {
            apartments = apartmentService.findByOwnerName(ownerName).stream()
                    .map(apartmentMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (address != null) {
            apartments = apartmentService.findByAddressContaining(address).stream()
                    .map(apartmentMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (size != null) {
            apartments = apartmentService.findBySizeGreaterThan(size).stream()
                    .map(apartmentMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (minPrice != null && maxPrice != null) {
            apartments = apartmentService.findByRentalPriceBetween(minPrice, maxPrice).stream()
                    .map(apartmentMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            apartments = apartmentService.getAllApartments().stream()
                    .map(apartmentMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(apartments);
    }
}