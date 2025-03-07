package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ug.NestPoint.dto.RentalDTO;
import pl.ug.NestPoint.mapper.RentalMapper;
import pl.ug.NestPoint.service.RentalService;

import java.util.List;

@Controller
@RequestMapping("/rentals/form")
@RequiredArgsConstructor
public class RentalFormController {
    private final RentalService rentalService;
    private final RentalMapper rentalMapper; // <- DODANE

    @GetMapping("/create")
    public String showCreateRentalForm(Model model) {
        model.addAttribute("rental", new RentalDTO());
        return "add-rental";
    }

    @PostMapping("/create")
    public String createRental(RentalDTO rentalDTO, Model model) {
        rentalService.createRental(rentalDTO);
        model.addAttribute("rental", rentalDTO);
        return "redirect:/rentals";
    }

    @GetMapping("/edit/{id}")
    public String showEditRentalForm(@PathVariable Long id, Model model) {
        var rental = rentalService.getRentalById(id);
        if (rental == null) {
            return "redirect:/rentals";
        }
        model.addAttribute("rental", rentalMapper.toDTO(rental));
        return "rental-form";
    }

    @PostMapping("/edit/{id}")
    public String updateRental(@PathVariable Long id, RentalDTO rentalDTO, Model model) {
        rentalService.updateRental(id, rentalDTO);
        model.addAttribute("rental", rentalDTO);
        return "redirect:/rentals";
    }

    @GetMapping("/delete")
    public String showDeleteRentalForm(Model model) {
        List<RentalDTO> rentals = rentalService.getAllRentals().stream()
                .map(rentalMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("rentals", rentals);
        return "delete-rental";
    }

    @PostMapping("/delete/{id}")
    public String deleteRental(@PathVariable Long id) {
        rentalService.deleteRental(id);
        return "redirect:/rentals/form/delete";
    }
}
