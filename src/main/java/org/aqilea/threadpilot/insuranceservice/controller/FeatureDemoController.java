package org.aqilea.threadpilot.insuranceservice.controller;


import io.swagger.v3.oas.annotations.Parameter;
import org.aqilea.threadpilot.insuranceservice.service.FeatureService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feature")
public class FeatureDemoController {
    private final FeatureService featureService;

    public FeatureDemoController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping("/{featureName}")
    public boolean featureDemo(@Parameter @PathVariable String featureName) {
        return featureService.isBusinessFeatureEnabled(featureName);
    }
}
