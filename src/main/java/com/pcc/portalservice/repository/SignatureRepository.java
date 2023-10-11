package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Signature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignatureRepository extends JpaRepository<Signature,Long> {
    Optional<Signature> findByUserId(Long userId);
}
