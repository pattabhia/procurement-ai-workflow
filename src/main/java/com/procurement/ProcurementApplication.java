package com.procurement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ProcurementApplication {

    private static final Logger log = LoggerFactory.getLogger(ProcurementApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ProcurementApplication.class, args);
    }

    /**
     * Basic startup hook – confirm app boot details.
     */
    @Bean
    CommandLineRunner onStart() {
        return args -> log.info("✅ Procurement AI Workflow started. Java={}, ActiveProfiles={}",
                System.getProperty("java.version"),
                System.getProperty("spring.profiles.active", "default"));
    }

    /**
     * ObjectMapper configured for Java time (ISO-8601) and sane defaults.
     */
    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Add common tags to all Micrometer metrics (helps in Prometheus/Grafana).
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags(
                "app", "procurement-ai-workflow",
                "env", System.getProperty("spring.profiles.active", "dev")
        );
    }

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }
}
