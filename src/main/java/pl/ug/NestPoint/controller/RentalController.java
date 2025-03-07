package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ug.NestPoint.dto.RentalDTO;
import pl.ug.NestPoint.dto.RentalSearchCriteria;
import pl.ug.NestPoint.mapper.RentalMapper;
import pl.ug.NestPoint.service.RentalService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals().stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Long id) {
        RentalDTO rentalDTO = rentalMapper.toDTO(rentalService.getRentalById(id));
        if (rentalDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rentalDTO);
    }

    @PostMapping
    public ResponseEntity<RentalDTO> createRental(@RequestBody RentalDTO rentalDTO) {
        rentalService.createRental(rentalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RentalDTO> updateRental(@PathVariable Long id, @RequestBody RentalDTO rentalDTO) {
        rentalService.updateRental(id, rentalDTO);
        return ResponseEntity.ok(rentalDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<RentalDTO>> searchRentals(@RequestBody RentalSearchCriteria criteria,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "id") String sortBy,
                                                         @RequestParam(defaultValue = "ASC") String direction) {
        Page<RentalDTO> rentals = rentalService
                .searchRentals(criteria, page, size, sortBy, direction)
                .map(rentalMapper::toDTO);
        return ResponseEntity.ok(rentals);
    }
}