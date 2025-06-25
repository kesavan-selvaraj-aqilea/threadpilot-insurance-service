package org.aqilea.threadpilot.insuranceservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.concurrent.atomic.AtomicReference;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Insurance {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String personalIdentificationNumber;

    @NotBlank
    private String type;

    @NotBlank
    private double monthlyCost;

    @JsonIgnore
    @Column(name = "vehicle_registration_number")
    private String vehicleRegistrationNumber;

    @OneToOne
    @JoinColumn(name = "registration_number", referencedColumnName = "registrationNumber")
    private Vehicle vehicle;

    private Boolean discountApplied;

    private String discountCampaign;
}
