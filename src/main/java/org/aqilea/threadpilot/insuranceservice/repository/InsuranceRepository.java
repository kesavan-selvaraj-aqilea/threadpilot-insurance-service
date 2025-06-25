package org.aqilea.threadpilot.insuranceservice.repository;

import org.aqilea.threadpilot.insuranceservice.model.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    List<Insurance> findByPersonalIdentificationNumber(String personalIdentificationNumber);
}
