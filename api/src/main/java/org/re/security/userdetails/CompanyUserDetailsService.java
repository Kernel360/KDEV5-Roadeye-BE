package org.re.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.re.employee.domain.EmployeeRepository;
import org.re.company.supports.CompanyIdProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final CompanyIdProvider companyIdProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var companyId = companyIdProvider.getCurrentCompanyId();
        var user = employeeRepository.findByUsernameAndCompanyId(companyId, username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return CompanyUserDetails.from(user);
    }
}
