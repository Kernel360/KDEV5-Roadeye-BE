package org.re.bootstrap;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Getter
@ConfigurationProperties(prefix = "roadeye.bootstrap")
public class BootstrapperProperty {
    private final Integer members;
    private final Integer cars;

    @ConstructorBinding
    public BootstrapperProperty(
        @DefaultValue("10") Integer members,
        @DefaultValue("500") Integer cars
    ) {
        this.members = members;
        this.cars = cars;
    }
}
