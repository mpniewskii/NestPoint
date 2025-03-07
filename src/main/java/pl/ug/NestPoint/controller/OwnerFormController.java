package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ug.NestPoint.domain.Owner;
import pl.ug.NestPoint.dto.OwnerDTO;
import pl.ug.NestPoint.mapper.OwnerMapper;
import pl.ug.NestPoint.service.OwnerService;

import java.util.List;

@Controller
@RequestMapping("/owners/form")
@RequiredArgsConstructor
public class OwnerFormController {
    private final OwnerService ownerService;
    private final OwnerMapper ownerMapper;

    @GetMapping("/create")
    public String showCreateOwnerForm(Model model) {
        model.addAttribute("owner", new OwnerDTO());
        return "add-owner";
    }

    @PostMapping("/create")
    public String createOwner(OwnerDTO ownerDTO, Model model) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        ownerService.createOwner(owner);
        model.addAttribute("owner", ownerMapper.toDTO(owner));
        return "redirect:/owners";
    }

    @GetMapping("/edit/{id}")
    public String showEditOwnerForm(@PathVariable Long id, Model model) {
        Owner owner = ownerService.getOwnerById(id);
        if (owner == null) {
            return "redirect:/owners";
        }
        model.addAttribute("owner", ownerMapper.toDTO(owner));
        return "owner-form";
    }

    @PostMapping("/edit/{id}")
    public String updateOwner(@PathVariable Long id, OwnerDTO ownerDTO, Model model) {
        Owner updatedOwner = ownerMapper.toEntity(ownerDTO);
        ownerService.updateOwner(id, updatedOwner);
        model.addAttribute("owner", ownerMapper.toDTO(updatedOwner));
        return "redirect:/owners";
    }

    @GetMapping("/delete")
    public String showDeleteOwnerForm(Model model) {
        List<OwnerDTO> owners = ownerService.getAllOwners().stream()
                .map(ownerMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("owners", owners);
        return "delete-owner";
    }

    @PostMapping("/delete/{id}")
    public String deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return "redirect:/owners/form/delete";
    }
}