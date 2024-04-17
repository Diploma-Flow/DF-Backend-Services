package org.simo.messageservice.helo;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @GetMapping("/hello")
    public String getHello(@RequestHeader("token-X") String token){
        return "Hello from message service controller. with token: " + token;
    }

    @GetMapping("/testing")
    public String getTesting(){
        log.info("Reached message service controller.");
        return "Hello from message service";
    }
}
