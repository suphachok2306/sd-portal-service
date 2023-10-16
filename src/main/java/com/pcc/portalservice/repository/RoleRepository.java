package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.enums.Roles;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRole(Roles role);
  boolean existsByRole(Roles role);
}
