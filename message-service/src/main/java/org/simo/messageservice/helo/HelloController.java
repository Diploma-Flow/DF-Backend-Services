package org.simo.messageservice.helo;

import lombok.extern.log4j.Log4j2;
import org.simo.auth.context.provider.RequestContext;
import org.simo.auth.context.provider.RequestContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Simeon Popov
 * Date of creation: 7.1.2024 г..
 */

@Log4j2
@RestController
@RequestMapping("/message")
public class HelloController {

    @GetMapping("/testing")
    public String getTesting() {
        RequestContext requestContext = RequestContextHolder.getContext();
        String userEmail = requestContext.getUserEmail();
        String userRole = requestContext.getUserRole();

        log.info("X-User-Email: {}", userEmail);
        log.info("X-User-Role: {}", userRole);
        return "Hello from message service";
    }
}
