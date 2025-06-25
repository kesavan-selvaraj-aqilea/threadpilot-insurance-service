package org.aqilea.threadpilot.insuranceservice.config;

import io.getunleash.DefaultUnleash;
import io.getunleash.Unleash;
import io.getunleash.util.UnleashConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnleashConfiguration {

    @Bean
    public UnleashConfig unleashConfig(
            @Value("${unleash.app-name}") String appName,
            @Value("${unleash.instance-id}") String instanceId,
            @Value("${unleash.api-url}") String apiUrl,
            @Value("${unleash.api-key}") String apiKey
    ) {
        return UnleashConfig.builder()
                .appName(appName)
                .instanceId(instanceId)
                .unleashAPI(apiUrl)
                .apiKey(apiKey)
                .build();
    }

    @Bean
    public Unleash unleash(UnleashConfig config) {
        return new DefaultUnleash(config);
    }
}
