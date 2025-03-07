package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ug.NestPoint.domain.Owner;
import pl.ug.NestPoint.dto.OwnerDTO;
import pl.ug.NestPoint.mapper.OwnerMapper;
import pl.ug.NestPoint.service.OwnerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;

    @GetMapping
    public ResponseEntity<List<OwnerDTO>> getAllOwners() {
        List<OwnerDTO> owners = ownerService.getAllOwners().stream()
                .map(ownerMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDTO> getOwnerById(@PathVariable Long id) {
        Owner owner = ownerService.getOwnerById(id);
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ownerMapper.toDTO(owner));
    }

    @PostMapping
    public ResponseEntity<OwnerDTO> createOwner(@RequestBody OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        ownerService.createOwner(owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerMapper.toDTO(owner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDTO> updateOwner(@PathVariable Long id, @RequestBody OwnerDTO ownerDTO) {
        Owner updatedOwner = ownerMapper.toEntity(ownerDTO);
        ownerService.updateOwner(id, updatedOwner);
        return ResponseEntity.ok(ownerMapper.toDTO(updatedOwner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<OwnerDTO>> searchOwners(@RequestParam(required = false) Boolean occupied,
                                                       @RequestParam(required = false) String address,
                                                       @RequestParam(required = false) Double rentalPrice) {
        List<OwnerDTO> owners;
        if (occupied != null) {
            owners = ownerService.findOwnersByApartmentOccupied(occupied).stream()
                    .map(ownerMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (address != null) {
            owners = ownerService.findOwnersByApartmentAddress(address).stream()
                    .map(ownerMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (rentalPrice != null) {
            owners = ownerService.findOwnersByApartmentRentalPriceGreaterThan(rentalPrice).stream()
                    .map(ownerMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            owners = ownerService.getAllOwners().stream()
                    .map(ownerMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/{id}/fee-percentage")
    public ResponseEntity<List<Object[]>> getOwnerFeePercentage(@PathVariable Long id) {
        List<Object[]> feePercentage = ownerService.getOwnerFeePercentage(id);
        return ResponseEntity.ok(feePercentage);
    }

    @GetMapping("/fee-percentage-by-address")
    public ResponseEntity<List<Object[]>> getOwnerFeePercentageByAddress(@RequestParam String address) {
        List<Object[]> feePercentage = ownerService.getOwnerFeePercentageByAddress(address);
        return ResponseEntity.ok(feePercentage);
    }
}