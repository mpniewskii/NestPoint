package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ug.NestPoint.domain.Apartment;
import pl.ug.NestPoint.dto.ApartmentDTO;
import pl.ug.NestPoint.mapper.ApartmentMapper;
import pl.ug.NestPoint.service.ApartmentService;

import java.util.List;

@Controller
@RequestMapping("/apartments/form")
@RequiredArgsConstructor
public class ApartmentFormController {
    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;

    @GetMapping("/create")
    public String showCreateApartmentForm(Model model) {
        model.addAttribute("apartment", new ApartmentDTO());
        return "add-apartment";
    }

    @PostMapping("/create")
    public String createApartment(ApartmentDTO apartmentDTO, Model model) {
        Apartment apartment = apartmentMapper.toEntity(apartmentDTO);
        apartmentService.createApartment(apartment);
        model.addAttribute("apartment", apartmentMapper.toDTO(apartment));
        return "redirect:/apartments";
    }

    @GetMapping("/edit/{id}")
    public String showEditApartmentForm(@PathVariable Long id, Model model) {
        Apartment apartment = apartmentService.getApartmentById(id);
        if (apartment == null) {
            return "redirect:/apartments";
        }
        model.addAttribute("apartment", apartmentMapper.toDTO(apartment));
        return "add-apartment";
    }

    @PostMapping("/edit/{id}")
    public String updateApartment(@PathVariable Long id, ApartmentDTO apartmentDTO, Model model) {
        Apartment updatedApartment = apartmentMapper.toEntity(apartmentDTO);
        apartmentService.updateApartment(id, updatedApartment);
        model.addAttribute("apartment", apartmentMapper.toDTO(updatedApartment));
        return "redirect:/apartments";
    }

    @GetMapping("/delete")
    public String showDeleteApartmentForm(Model model) {
        List<ApartmentDTO> apartments = apartmentService.getAllApartments().stream()
                .map(apartmentMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("apartments", apartments);
        return "delete-apartment";
    }

    @PostMapping("/delete/{id}")
    public String deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return "redirect:/apartments/form/delete";
    }
}