package com.pcc.portalservice.service;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.repository.RoleRepository;
import com.pcc.portalservice.repository.UserRepository;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.UpdateUserRequest;
import com.pcc.portalservice.specs.UserSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    // Services
//    private final TeamService teamService;
//    private final UserTeamRelationService userTeamRelationService;
    private final AuthenticationService authenticationService;
    // Repositories
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // find User by user.email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    // find User by Id
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    public User update(Long id, UpdateUserRequest updateUserRequest) {
        User user = findById(id);

        user.setUsername(updateUserRequest.getUsername());
        user.setEmail(updateUserRequest.getEmail());
        user.setFirstname(updateUserRequest.getFirstname());
        user.setLastname(updateUserRequest.getLastname());
        user.setTelephone((updateUserRequest.getTelephone()));
        //  TODO: Also implement Passwort update

        return userRepository.save(user);
    }

    // find all users - paged
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> searchAll(Pageable pageable, String username) {
        return userRepository.findAll(username != null ? UserSpecs.usernameLike(username) : null, pageable);
    }

//    public void addTeamToUser(Long teamId, Long userId) {
//        Team team = teamService.findById(teamId);
//        User user = findById(userId);
//        // Role for a new User is always the MEMBER Role
//        Role role = roleRepository.findByRole(Roles.MEMBER).orElseThrow(() -> new RuntimeException("Not found"));
//
//        UserTeamRelation userTeamRelation = userTeamRelationService.build(user, team, role);
//
//        // check if user already in team
//        if (findById(teamId).getTeams().contains(userTeamRelation)) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already in team");
//        }
//
//        userTeamRelationService.save(userTeamRelation);
//    }


    public User create(CreateUserRequest createUserRequest) {

        // Check Email
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new RuntimeException("Email is already in use.");
        }

        // Check Username
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new RuntimeException("Username is already in use.");
        }

        String hashedPassword = authenticationService.hashPassword(createUserRequest.getPassword());

        // Create User
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .firstname(createUserRequest.getFirstname())
                .lastname(createUserRequest.getLastname())
                .password(hashedPassword)
                .username(createUserRequest.getUsername())
                .telephone(createUserRequest.getTelephone())
                .roles(new ArrayList<>())
                .build();
        for (String roleName : createUserRequest.getRoles()) {
            Roles roleEnum = Roles.valueOf(roleName); // Convert role name to enum
            Role role = roleRepository.findByRole(roleEnum).orElseThrow(()
                    -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleName));
            user.getRoles().add(role); // Add the role to the user's roles
            }
        return userRepository.save(user);
    }

    public Role createRole(Roles roleName) {
        Role role = Role.builder().role(roleName).build();
        return roleRepository.save(role);
    }

    public void addRoleToUser(Long userId, Roles roleName) {
        User user = findById(userId);
        Role role = roleRepository.findByRole(roleName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleName));

        user.getRoles().add(role);
        userRepository.save(user);
    }



}

