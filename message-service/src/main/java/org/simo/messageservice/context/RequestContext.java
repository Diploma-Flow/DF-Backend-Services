package org.simo.messageservice.context;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Simeon Popov
 * Date of creation: 6/11/2024
 */

@Getter
@Setter
public class RequestContext {
    private String userEmail;
    private String userRole;
}
