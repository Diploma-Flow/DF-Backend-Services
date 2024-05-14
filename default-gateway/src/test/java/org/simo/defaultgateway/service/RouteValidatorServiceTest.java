package org.simo.defaultgateway.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.simo.defaultgateway.properties.PublicRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

/**
 * Author: Simeon Popov
 * Date of creation: 5/13/2024
 */

@ExtendWith(MockitoExtension.class)
class RouteValidatorServiceTest {

    @Mock
    private PublicRoutes publicRoutes;

    @InjectMocks
    private RouteValidatorService routeValidatorService;

    @Test
    public void testIsRouteProtected_PublicRoute() {
        // Mock publicRoutes to return a list containing a matching route
        List<String> publicRouteList = Collections.singletonList("/public/**");
        Mockito.when(publicRoutes.getRoutes()).thenReturn(publicRouteList);

        String route = "/public/api/data";
        boolean isProtected = routeValidatorService.isRouteProtected(route);

        // Assert that the route is considered public (not protected)
        assertFalse(isProtected);
    }

    @Test
    public void testIsRouteProtected_NonPublicRoute() {
        // Mock publicRoutes to return an empty list or a list without a matching route
        Mockito.when(publicRoutes.getRoutes()).thenReturn(Collections.emptyList());

        String route = "/private/api/data";
        boolean isProtected = routeValidatorService.isRouteProtected(route);

        // Assert that the route is considered protected (no public match)
        assertTrue(isProtected);
    }

    @Test
    public void testIsRoutePublic_PublicRoute() {
        // Mock publicRoutes to return a list containing a matching route
        List<String> publicRouteList = Collections.singletonList("/public/**");
        Mockito.when(publicRoutes.getRoutes()).thenReturn(publicRouteList);

        String route = "/public/api/data";
        boolean isPublic = routeValidatorService.isRoutePublic(route);

        // Assert that the route is considered public (match found)
        assertTrue(isPublic);
    }

    @Test
    public void testIsRoutePublic_NonPublicRoute() {
        // Mock publicRoutes to return an empty list or a list without a matching route
        Mockito.when(publicRoutes.getRoutes()).thenReturn(Collections.emptyList());

        String route = "/private/api/data";
        boolean isPublic = routeValidatorService.isRoutePublic(route);

        // Assert that the route is considered non-public (no match found)
        assertFalse(isPublic);
    }

}