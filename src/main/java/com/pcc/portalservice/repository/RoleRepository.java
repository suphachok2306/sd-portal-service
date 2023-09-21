package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
//public interface RoleRepository extends JpaRepository<Role, Long> {
//    Role findByName(String name);
//}

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRole(Roles role);
}
