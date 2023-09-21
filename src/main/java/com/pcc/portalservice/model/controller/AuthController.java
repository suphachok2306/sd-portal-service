package com.pcc.portalservice.model.controller;

import com.pcc.portalservice.model.User;
import com.pcc.portalservice.requests.JwtAuthenticationResponse;
import com.pcc.portalservice.requests.LoginRequest;
import com.pcc.portalservice.service.AuthenticationService;
import com.pcc.portalservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@BasePathAwareController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    @Transactional
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        JwtAuthenticationResponse jwtAuthenticationResponse = authenticationService.login(user, loginRequest.getPassword());
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }

}
