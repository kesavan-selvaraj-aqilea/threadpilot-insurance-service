package org.aqilea.threadpilot.insuranceservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.commons.lang3.StringUtils;
import org.aqilea.threadpilot.insuranceservice.handlers.exceptions.PersonNotFoundException;
import org.aqilea.threadpilot.insuranceservice.model.Insurance;
import org.aqilea.threadpilot.insuranceservice.model.InsuranceType;
import org.aqilea.threadpilot.insuranceservice.model.Vehicle;
import org.aqilea.threadpilot.insuranceservice.repository.InsuranceRepository;
import org.aqilea.threadpilot.insuranceservice.service.FeatureService;
import org.aqilea.threadpilot.insuranceservice.service.InsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    private static final Logger log = LoggerFactory.getLogger(InsuranceServiceImpl.class);
    private final RestTemplate restTemplate;
    private final Environment environment;
    private final Map<String, List<Insurance>> personInsurancesData = new HashMap<>();
    private final FeatureService featureService;
    private final InsuranceRepository insuranceRepository;
    @Value("${vehicle.service.url:http://localhost:8081/vehicles/}")
    private String vehicleServiceBaseUrl;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public InsuranceServiceImpl(RestTemplate restTemplate, Environment environment,
                                InsuranceRepository insuranceRepository, FeatureService featureService) {
        this.restTemplate = restTemplate;
        this.environment = environment;
        this.insuranceRepository = insuranceRepository;
        this.featureService = featureService;
    }

    private static boolean isPersonalHealthInsurance(String type) {
        return InsuranceType.PERSONAL_HEALTH_INSURANCE.getDisplayName().equals(type);
    }

    private static boolean isCarInsurance(String type) {
        return InsuranceType.CAR_INSURANCE.getDisplayName().equals(type);
    }

    @Override
    public List<Insurance> getPersonInsurances(String personalIdentificationNumber) {
        List<Insurance> insurances = insuranceRepository.findByPersonalIdentificationNumber(personalIdentificationNumber);

        if (insurances == null || insurances.isEmpty()) {
            throw new PersonNotFoundException("Person with ID '" + personalIdentificationNumber + "' not found or has no insurances.");
        }

        boolean isBusinessCampaignDiscountEnabled = featureService.isBusinessFeatureEnabled("business-campaign-discount");

        List<Insurance> resultInsurances = new ArrayList<>();
        for (Insurance insurance : insurances) {
            String type = insurance.getType();
            Double monthlyCost = insurance.getMonthlyCost();
            Vehicle carInfo = null;
            Boolean discountApplied = null;
            String discountCampaign = null;

            // Apply business campaign discount if enabled and customer is eligible
            if (isBusinessCampaignDiscountEnabled && isEligibleForBusinessCampaignDiscount(type)) {
                if (monthlyCost != null) {
                    monthlyCost *= 0.9; // 10% discount
                    discountApplied = true;
                    discountCampaign = "Summer Savings 2025";
                }
            }

            // Integrate with Endpoint 1 (Vehicle Service) if it's car insurance
            if (isCarInsurance(type)) {
                String registrationNumber = insurance.getVehicleRegistrationNumber();
                String vehicleServiceUrl = vehicleServiceBaseUrl + '/' + StringUtils.upperCase(registrationNumber);
                log.info("Insurance service url: " + vehicleServiceUrl);
                try {
                    ResponseEntity<Map> response = getVehicleInfo(vehicleServiceUrl);
                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                        carInfo = objectMapper.convertValue(response.getBody(), Vehicle.class);
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching car info for " + registrationNumber + ": " + e.getMessage());
                }
            }

            resultInsurances.add(Insurance.builder()
                    .personalIdentificationNumber(insurance.getPersonalIdentificationNumber())
                    .type(type)
                    .monthlyCost(monthlyCost != null ? monthlyCost : 0.0)
                    .vehicle(carInfo)
                    .vehicleRegistrationNumber(insurance.getVehicleRegistrationNumber())
                    .discountApplied(discountApplied)
                    .discountCampaign(discountCampaign)
                    .build());
        }
        return resultInsurances;
    }

    @CircuitBreaker(name = "vehicleService", fallbackMethod = "getVehicleInfoFallback")
    private ResponseEntity<Map> getVehicleInfo(String vehicleServiceUrl) {
        return restTemplate.getForEntity(vehicleServiceUrl, Map.class);
    }

    public ResponseEntity<Map> getVehicleInfoFallback(String vehicleServiceUrl, Throwable throwable) {
        // Fallback logic: return an empty map or handle the error
        return ResponseEntity.ok(Collections.emptyMap());
    }

    private boolean isEligibleForBusinessCampaignDiscount(String type) {
        return isPersonalHealthInsurance(type) || isCarInsurance(type);
    }
}
