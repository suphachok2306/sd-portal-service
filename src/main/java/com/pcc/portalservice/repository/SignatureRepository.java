package com.pcc.portalservice.repository;

import com.pcc.portalservice.model.Signature;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureRepository extends JpaRepository<Signature, Long> {
  Optional<Signature> findByUserId(Long userId);
}
