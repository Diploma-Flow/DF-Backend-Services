package org.simo.defaultgateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.simo.defaultgateway.properties.PublicRoutes;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

/**
 * Author: Simeon Popov
 * Date of creation: 7.4.2024 г.
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class RouteValidatorService {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final PublicRoutes publicRoutes;

    public boolean isRouteProtected(String route) {
        return publicRoutes
                .getRoutes()
                .stream()
                .noneMatch(uri -> pathMatcher.match(uri, route));
    }

    public boolean isRoutePublic(String route) {
        return publicRoutes
                .getRoutes()
                .stream()
                .anyMatch(uri -> pathMatcher.match(uri, route));
    }
}
