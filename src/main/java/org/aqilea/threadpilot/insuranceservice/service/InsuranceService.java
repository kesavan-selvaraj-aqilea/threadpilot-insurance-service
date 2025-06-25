package org.aqilea.threadpilot.insuranceservice.service;

import org.aqilea.threadpilot.insuranceservice.model.Insurance;

import java.util.List;

public interface InsuranceService {
    List<Insurance> getPersonInsurances(String personalIdentificationNumber);
}
