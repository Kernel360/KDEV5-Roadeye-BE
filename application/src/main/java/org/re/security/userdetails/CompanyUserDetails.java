package org.re.security.userdetails;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.re.company.domain.CompanyId;
import org.re.employee.domain.Employee;
import org.re.employee.domain.EmployeeRole;
import org.re.security.AuthMemberType;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

@Getter
public class CompanyUserDetails implements UserDetails, CredentialsContainer {
    private static final Collection<? extends GrantedAuthority> DEFAULT_AUTHORITIES
        = Collections.unmodifiableCollection(AuthorityUtils.createAuthorityList(AuthMemberType.USER.getValue()));

    @NonNull
    private final CompanyId companyId;
    private final Long userId;

    private final String username;
    private String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private CompanyUserDetails(
        @NonNull CompanyId companyId,
        Long userId,
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled
    ) {
        this.companyId = companyId;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableCollection(authorities);
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static UserDetails from(Employee user) {
        var companyId = new CompanyId(user.getCompanyId());
        return new CompanyUserDetails(
            companyId,
            user.getId(),
            user.getCredentials().loginId(),
            user.getCredentials().password(),
            createAuthorities(user),
            true,
            true,
            true,
            true
        );
    }

    private static Collection<? extends GrantedAuthority> createAuthorities(Employee user) {
        return Stream.concat(
            DEFAULT_AUTHORITIES.stream(),
            Stream.of(user.getRole())
                .map((r) -> new SimpleGrantedAuthority(r.name()))
        ).toList();
    }

    public boolean isManager() {
        return authorities.stream()
            .anyMatch((a) -> a.getAuthority().equals(EmployeeRole.ROOT.name()));
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
