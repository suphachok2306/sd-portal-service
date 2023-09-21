package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // check if a user exists by searching for his email
    boolean existsByEmail(String email);
    // check if a user exists by searching for his username
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    public Page<User> findAll(Specification<User> spec, Pageable pageable);

}
