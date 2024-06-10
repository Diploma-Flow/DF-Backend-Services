package org.simo.messageservice.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Author: Simeon Popov
 * Date of creation: 6/11/2024
 */

@Component
public class RequestContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userEmail = request.getHeader("X-User-Email");
        String userRole = request.getHeader("X-User-Role");

        // Populate RequestContext with extracted details
        RequestContext requestContext = RequestContextHolder.getContext();
        requestContext.setUserEmail(userEmail);
        requestContext.setUserRole(userRole);

        return true; // Continue processing the request
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Clear the context after the request is completed
        RequestContextHolder.clear();
    }
}
