package pl.ug.NestPoint.domain;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 30, message = "Name must be at most 30 characters")
    private String name;

    @Size(max = 30, message = "Surname must be at most 30 characters")
    private String surname;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{4}$", message = "Invalid phone number format")
    private String phone;

    @Pattern(regexp = "^ID[0-9]{3}$", message = "Invalid identifier format")
    @Column(unique = true, nullable = false)
    private String identifier; // ID number

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<Rental> rentals;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Tenant(Long id, String name, String surname, String email, String phone, String identifier) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.identifier = identifier;
    }
}