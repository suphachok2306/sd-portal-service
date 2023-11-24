package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByEmail(String email);
  boolean existsByEmpCode(String empCode);
  Optional<User> findByEmail(String email);

  List<User> findByRolesRole(Roles role);

  Optional<User> findById(Long userId);

}
