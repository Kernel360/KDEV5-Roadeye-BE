package org.re.hq.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.re.hq.employee.domain.EmployeeRepository;
import org.re.hq.tenant.TenantIdProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final TenantIdProvider tenantIdProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var tenantId = tenantIdProvider.getCurrentTenantId();
        var user = employeeRepository.findByUsernameAndTenantId(tenantId, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return CompanyUserDetails.from(user);
    }
}
