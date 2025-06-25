package org.aqilea.threadpilot.insuranceservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aqilea.threadpilot.insuranceservice.model.Insurance;
import org.aqilea.threadpilot.insuranceservice.service.InsuranceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/insurances")
@Tag(name = "Insurance API", description = "Operations related to insurances")
public class InsuranceController {

    private static final Logger logger = LoggerFactory.getLogger(InsuranceController.class);

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @Operation(summary = "Get person info by person number")
    @GetMapping("/{personalIdentificationNumber}")
    public List<Insurance> getInsuranceInfo(@Parameter @PathVariable String personalIdentificationNumber) {
        logger.debug("Received request for person with personalIdentificationNumber: {}", personalIdentificationNumber);
        return insuranceService.getPersonInsurances(personalIdentificationNumber);
    }
}
