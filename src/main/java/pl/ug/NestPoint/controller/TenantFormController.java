package pl.ug.NestPoint.controller;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.ug.NestPoint.domain.Tenant;
import pl.ug.NestPoint.dto.TenantDTO;
import pl.ug.NestPoint.mapper.TenantMapper;
import pl.ug.NestPoint.service.TenantService;

import java.util.List;

@Controller
@RequestMapping("/tenants/form")
@RequiredArgsConstructor
public class TenantFormController {
    private final TenantService tenantService;
    private final TenantMapper tenantMapper;

    @GetMapping("/create")
    public String showCreateTenantForm(Model model) {
        model.addAttribute("tenant", new TenantDTO());
        return "add-tenant";
    }

    @PostMapping("/create")
    public String createTenant(TenantDTO tenantDTO, Model model) {
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenantService.createTenant(tenant);
        model.addAttribute("tenant", tenantMapper.toDTO(tenant));
        return "redirect:/tenants";
    }

    @GetMapping("/edit/{id}")
    public String showEditTenantForm(@PathVariable Long id, Model model) {
        Tenant tenant = tenantService.getTenantById(id);
        if (tenant == null) {
            return "redirect:/tenants";
        }
        model.addAttribute("tenant", tenantMapper.toDTO(tenant));
        return "tenant-form";
    }

    @PostMapping("/edit/{id}")
    public String updateTenant(@PathVariable Long id, TenantDTO tenantDTO, Model model) {
        Tenant updatedTenant = tenantMapper.toEntity(tenantDTO);
        tenantService.updateTenant(id, updatedTenant);
        model.addAttribute("tenant", tenantMapper.toDTO(updatedTenant));
        return "redirect:/tenants";
    }

    @GetMapping("/delete")
    public String showDeleteTenantForm(Model model) {
        List<TenantDTO> tenants = tenantService.getAllTenants().stream()
                .map(tenantMapper::toDTO)
                .collect(Collectors.toList());
        model.addAttribute("tenants", tenants);
        return "delete-tenant";
    }

    @PostMapping("/delete/{id}")
    public String deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return "redirect:/tenants/form/delete";
    }
}