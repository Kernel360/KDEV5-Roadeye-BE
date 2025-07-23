package org.re.admin.repository;

import org.re.admin.domain.PlatformAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlatformAdminRepository extends JpaRepository<PlatformAdmin, Long> {
    @Query("SELECT p FROM PlatformAdmin p WHERE p.loginInfo.username = :username AND p.deleted = false")
    Optional<PlatformAdmin> findByUsername(String username);
}
