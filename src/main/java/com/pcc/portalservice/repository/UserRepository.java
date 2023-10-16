package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // check if a user exists by searching for his email
  boolean existsByEmail(String email);
  // check if a user exists by searching for his username
  //boolean existsByUsername(String username);
  boolean existsByempCode(String empCode);
  Optional<User> findByEmail(String email);

  List<User> findByRolesRole(Roles role);

  Optional<User> findById(Long userId);
  //public Page<User> findAll(Specification<User> spec, Pageable pageable);

}
