package org.simo.dms.diplomamanagementservice.controller;

import lombok.extern.log4j.Log4j2;
import org.simo.auth.context.provider.RequestContext;
import org.simo.auth.context.provider.RequestContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Simeon Popov
 * Date of creation: 6/17/2024
 */

@Log4j2
@RestController
@RequestMapping("/diploma-management")
public class DiplomaManagementController {

    @GetMapping("/test")
    public ResponseEntity<String> testContext(){
        RequestContext requestContext = RequestContextHolder.getContext();
        String userEmail = requestContext.getUserEmail();
        String userRole = requestContext.getUserRole();

        log.info("X-User-Email: {}", userEmail);
        log.info("X-User-Role: {}", userRole);
        return ResponseEntity.ok("Hello from diploma management service");
    }
}
