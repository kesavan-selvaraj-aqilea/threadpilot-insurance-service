package org.aqilea.threadpilot.insuranceservice.service;

import io.getunleash.Unleash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FeatureService {

    private final Unleash unleash;
    private static final Logger logger = LoggerFactory.getLogger(FeatureService.class);

    public FeatureService(Unleash unleash) {
        this.unleash = unleash;
    }

    public boolean isBusinessFeatureEnabled(String featureName) {
        boolean isEnabled = unleash.isEnabled(featureName);
        logger.info("Feature '{}' is enabled: {}", featureName, isEnabled);
        return isEnabled;
    }
}
