package org.re.security.userdetails;

import lombok.RequiredArgsConstructor;
import org.re.admin.repository.PlatformAdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatformAdminUserDetailsService implements UserDetailsService {
    private final PlatformAdminRepository platformAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = platformAdminRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return PlatformAdminUserDetails.from(user);
    }
}
