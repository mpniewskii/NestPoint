package pl.ug.NestPoint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.ug.NestPoint.domain.Owner;
import pl.ug.NestPoint.domain.Tenant;
import pl.ug.NestPoint.domain.Apartment;
import pl.ug.NestPoint.domain.Rental;
import pl.ug.NestPoint.repository.OwnerRepository;
import pl.ug.NestPoint.repository.TenantRepository;
import pl.ug.NestPoint.repository.ApartmentRepository;
import pl.ug.NestPoint.repository.RentalRepository;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/admin/owners")
    public String viewOwners(Model model) {
        List<Owner> owners = ownerRepository.findAll();
        model.addAttribute("owners", owners);
        return "owners";
    }

    @GetMapping("/admin/tenants")
    public String viewTenants(Model model) {
        List<Tenant> tenants = tenantRepository.findAll();
        model.addAttribute("tenants", tenants);
        return "tenants";
    }

    @GetMapping("/admin/apartments")
    public String viewApartments(Model model) {
        List<Apartment> apartments = apartmentRepository.findAll();
        model.addAttribute("apartments", apartments);
        return "apartments";
    }

    @GetMapping("/admin/rentals")
    public String viewRentals(Model model) {
        List<Rental> rentals = rentalRepository.findAll();
        model.addAttribute("rentals", rentals);
        return "rentals";
    }
}