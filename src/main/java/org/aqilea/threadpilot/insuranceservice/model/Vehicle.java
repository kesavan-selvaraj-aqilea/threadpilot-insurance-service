package org.aqilea.threadpilot.insuranceservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Vehicle {
    @Id
    private String registrationNumber;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotBlank
    private String madeYear;
}
