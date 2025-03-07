package pl.ug.NestPoint.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Pattern(regexp = "^[a-zA-Z\\s]{1,50}$", message = "Invalid street name")
    private String street;

    @Pattern(regexp = "^[0-9]+(/[0-9]+)?$", message = "Invalid apartment number")
    private String apartmentNumber;

    @Pattern(regexp = "^[a-zA-Z\\s]{1,50}(\\s[a-zA-Z\\s]{1,50}){0,2}$", message = "Invalid city name")
    private String city;

    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postal code")
    private String postalCode;

    public String getFullAddress() {
        return street + " " + apartmentNumber + ", " + city + ", " + postalCode;
    }
}