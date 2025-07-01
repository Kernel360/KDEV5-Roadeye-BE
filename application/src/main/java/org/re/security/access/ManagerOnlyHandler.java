package org.re.security.access;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.re.security.userdetails.CompanyUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ManagerOnlyHandler {
    @Before("@annotation(org.re.security.access.ManagerOnly)")
    public void before() {
        var context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
            throw new AccessDeniedException("Access denied");
        }
        var authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Access denied");
        }
        var details = authentication.getPrincipal();
        if (!(details instanceof CompanyUserDetails userDetails)) {
            throw new AccessDeniedException("Access denied");
        }
        if (!userDetails.isManager()) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
