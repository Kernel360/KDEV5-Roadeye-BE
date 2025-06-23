package org.re;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("org.re")
public class RoadEyeBootstrapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoadEyeBootstrapperApplication.class, args);
    }
}
