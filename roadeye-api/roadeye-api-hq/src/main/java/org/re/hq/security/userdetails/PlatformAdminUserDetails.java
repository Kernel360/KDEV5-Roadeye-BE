package org.re.hq.security.userdetails;

import lombok.Getter;
import org.re.hq.admin.domain.PlatformAdmin;
import org.re.hq.security.domain.AuthMemberType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class PlatformAdminUserDetails implements UserDetails {
    private static final Collection<? extends GrantedAuthority> DEFAULT_AUTHORITIES
        = Collections.unmodifiableCollection(AuthorityUtils.createAuthorityList(AuthMemberType.ADMIN.getValue()));

    private final String username;
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    private PlatformAdminUserDetails(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean enabled
    ) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableCollection(authorities);
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static UserDetails from(PlatformAdmin user) {
        return new PlatformAdminUserDetails(
            user.getLoginInfo().username(),
            user.getLoginInfo().password(),
            DEFAULT_AUTHORITIES,
            true,
            true,
            true,
            true
        );
    }
}
