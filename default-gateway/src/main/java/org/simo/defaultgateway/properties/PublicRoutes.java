package org.simo.defaultgateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 17.4.2024 г.
 */

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "public")
public class PublicRoutes {
    private List<String> routes;
}
