package org.simo.defaultgateway.filters;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * Author: Simeon Popov
 * Date of creation: 7.4.2024 г.
 */

@Component
public class RouteValidator {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> PUBLIC_API_ENDPOINTS = List.of(
            "/auth/**"
    );

    public boolean isRouteProtected(String route) {
        return PUBLIC_API_ENDPOINTS
                .stream()
                .noneMatch(uri -> pathMatcher.match(uri, route));
    }

    public boolean isRoutePublic(String route) {
        return PUBLIC_API_ENDPOINTS
                .stream()
                .anyMatch(uri -> pathMatcher.match(uri, route));
    }
}
