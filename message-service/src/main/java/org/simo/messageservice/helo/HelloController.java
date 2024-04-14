package org.simo.messageservice.helo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Simeon Popov
 * Date of creation: 7.1.2024 г..
 */

@RestController
@RequestMapping("/message")
public class HelloController {

    @GetMapping("/hello")
    public String getHello(@RequestHeader("token-X") String token){
        return "Hello from message service controller. with token: " + token;
    }

    @GetMapping("/testing")
    public String getTesting(){
        return "Hello from message service";
    }
}
