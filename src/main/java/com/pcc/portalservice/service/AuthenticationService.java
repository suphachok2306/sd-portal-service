package com.pcc.portalservice.service;

import com.pcc.portalservice.config.JwtTokenProvider;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.response.JwtAuthenticationResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  public JwtAuthenticationResponse login(User user, String plainPassword) {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(user.getId(), plainPassword)
      );
    } catch (AuthenticationException e) {
      throw new ResponseStatusException(
        HttpStatus.UNPROCESSABLE_ENTITY,
        "Invalid username/password supplied"
      );
    }

    String jwtToken = jwtTokenProvider.createToken(user);

    return JwtAuthenticationResponse
      .builder()
      .accessToken(jwtToken)
      .tokenType("BEARER")
      .build();
  }

  public String hashPassword(String plaingPassword) {
    return passwordEncoder.encode(plaingPassword);
  }
}
