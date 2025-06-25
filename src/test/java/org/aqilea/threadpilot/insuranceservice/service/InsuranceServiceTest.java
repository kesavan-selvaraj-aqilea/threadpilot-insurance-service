package org.aqilea.threadpilot.insuranceservice.service;

import org.aqilea.threadpilot.insuranceservice.handlers.exceptions.PersonNotFoundException;
import org.aqilea.threadpilot.insuranceservice.model.Insurance;
import org.aqilea.threadpilot.insuranceservice.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class InsuranceServiceTest {

    @Autowired
    private InsuranceService insuranceService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testGetPersonInsurances_Success_NoDiscount_NoCarInfo() {
        List<Insurance> insurances = insuranceService.getPersonInsurances("19850505-5678");
        assertEquals(1, insurances.size());
        Insurance insurance = insurances.get(0);
        assertEquals("Personal health insurance", insurance.getType());
        assertEquals(20.0, insurance.getMonthlyCost());
        assertNull(insurance.getVehicle());
        assertNull(insurance.getDiscountApplied());
        assertNull(insurance.getDiscountCampaign());
    }

    @Test
    void testGetPersonInsurances_Success_WithCarInfo() {
        Map<String, String> vehicleMap = new HashMap<>();
        vehicleMap.put("registrationNumber", "ABC123Z");
        vehicleMap.put("make", "Toyota");
        vehicleMap.put("model", "Corolla");
        vehicleMap.put("year", "2020");
        when(restTemplate.getForEntity(contains("ABC123Z"), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(vehicleMap, HttpStatus.OK));

        List<Insurance> insurances = insuranceService.getPersonInsurances("19900101-1234");
        Optional<Insurance> carInsurance = insurances.stream()
                .filter(i -> "Car insurance".equals(i.getType()))
                .findFirst();
        assertTrue(carInsurance.isPresent());
        Vehicle vehicle = carInsurance.get().getVehicle();
        assertNotNull(vehicle);
        assertEquals("ABC123Z", vehicle.getRegistrationNumber());
        assertEquals("Toyota", vehicle.getMake());
        assertEquals("Corolla", vehicle.getModel());
        assertEquals("2020", vehicle.getMadeYear());
    }

    @Test
    void testGetPersonInsurances_Success_WithDiscount() {
        System.setProperty("features.business-campaign-discount.enabled", "true");
        System.setProperty("features.business-campaign-discount.eligible-pids", "19900101-1234");

        List<Insurance> insurances = insuranceService.getPersonInsurances("19900101-1234");
        for (Insurance insurance : insurances) {
            if ("Car insurance".equals(insurance.getType()) || "Personal health insurance".equals(insurance.getType())) {
                assertEquals(true, insurance.getDiscountApplied());
                assertEquals("Summer Savings 2025", insurance.getDiscountCampaign());
                assertEquals(insurance.getType().equals("Car insurance") ? 27.0 : 18.0, insurance.getMonthlyCost());
            } else {
                assertNull(insurance.getDiscountApplied());
                assertNull(insurance.getDiscountCampaign());
            }
        }
    }

    @Test
    void testGetPersonInsurances_PersonNotFound() {
        assertThrows(PersonNotFoundException.class, () -> {
            insuranceService.getPersonInsurances("00000000-0000");
        });
    }

    @Test
    void testGetPersonInsurances_CarInfoServiceFails() {
        when(restTemplate.getForEntity(contains("XYZ789A"), eq(Map.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        List<Insurance> insurances = insuranceService.getPersonInsurances("19751122-9876");
        Optional<Insurance> carInsurance = insurances.stream()
                .filter(i -> "Car insurance".equals(i.getType()))
                .findFirst();
        assertTrue(carInsurance.isPresent());
        assertNull(carInsurance.get().getVehicle());
    }

    @Test
    void testGetPersonInsurances_EmptyInsuranceList() {
        // Simulate a person with no insurances (not present in database)
        assertThrows(PersonNotFoundException.class, () -> {
            insuranceService.getPersonInsurances("20000101-0000");
        });
    }

    @Test
    void testGetPersonInsurances_NullPersonalIdentificationNumber() {
        assertThrows(PersonNotFoundException.class, () -> {
            insuranceService.getPersonInsurances(null);
        });
    }

    @Test
    void testGetPersonInsurances_InvalidPersonalIdentificationNumberFormat() {
        assertThrows(PersonNotFoundException.class, () -> {
            insuranceService.getPersonInsurances("INVALID_ID");
        });
    }

    @Test
    void testGetPersonInsurances_NoDiscountForIneligiblePerson() {
        System.setProperty("features.business-campaign-discount.enabled", "true");
        System.setProperty("features.business-campaign-discount.eligible-pids", "19850505-5678");

        List<Insurance> insurances = insuranceService.getPersonInsurances("19900101-1234");
        for (Insurance insurance : insurances) {
            assertNull(insurance.getDiscountApplied());
            assertNull(insurance.getDiscountCampaign());
        }
    }

    @Test
    void testGetPersonInsurances_VehicleServiceTimeout() {
        when(restTemplate.getForEntity(contains("ABC123Z"), eq(Map.class)))
                .thenThrow(new RuntimeException("Timeout"));

        List<Insurance> insurances = insuranceService.getPersonInsurances("19900101-1234");
        Optional<Insurance> carInsurance = insurances.stream()
                .filter(i -> "Car insurance".equals(i.getType()))
                .findFirst();
        assertTrue(carInsurance.isPresent());
        assertNull(carInsurance.get().getVehicle());
    }

    @Test
    void testCircuitBreakerFallbackIsUsedWhenVehicleServiceFails() {
        // Simulate vehicle service failure
        when(restTemplate.getForEntity(contains("ABC123Z"), eq(Map.class)))
                .thenThrow(new RuntimeException("Simulated service failure"));

        List<Insurance> insurances = insuranceService.getPersonInsurances("19900101-1234");
        Optional<Insurance> carInsurance = insurances.stream()
                .filter(i -> "Car insurance".equals(i.getType()))
                .findFirst();

        assertTrue(carInsurance.isPresent());
        // Fallback returns empty vehicle info, so Vehicle should be null
        assertNull(carInsurance.get().getVehicle());
    }
}
