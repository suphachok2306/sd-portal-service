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

  //private final RefreshTokenRepository refreshTokenRepository;

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
    //RefreshToken refreshToken = createRefreshToken(user.getId());

    return JwtAuthenticationResponse
      .builder()
      .accessToken(jwtToken)
      //.refreshToken(refreshToken)
      .tokenType("BEARER")
      .build();
  }

  /*
   * Here we handle all the stuff for refresh tokens
   */
  //    public RefreshToken createRefreshToken(Long userId) {
  //        // @Value("${security.token.refresh.duration}")
  //        long refreshTokenDurationMs = 11111L;
  //
  //        RefreshToken refreshToken = new RefreshToken();
  //        refreshToken.setUserId(userId);
  //        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
  //        refreshToken.setToken(UUID.randomUUID().toString());
  //        return refreshTokenRepository.save(refreshToken);
  //    }

  //    public RefreshToken verifyExpiration(RefreshToken token) {
  //        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
  //            refreshTokenRepository.delete(token);
  //            throw new RuntimeException("Refresh token was expired. Please make a new login request");
  //        }
  //        return token;
  //    }

  // hash password
  public String hashPassword(String plaingPassword) {
    return passwordEncoder.encode(plaingPassword);
  }
}
