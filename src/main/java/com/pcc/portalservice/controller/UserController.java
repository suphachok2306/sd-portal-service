package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.UpdateUserRequest;
import com.pcc.portalservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> findAll(Pageable pageable, @RequestParam(required = false) String username, PagedResourcesAssembler pagedResourcesAssembler, PersistentEntityResourceAssembler persistentEntityResourceAssembler) {
        Page<User> usersPage = userService.searchAll(pageable, username);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(usersPage, persistentEntityResourceAssembler));
    }

    // TODO: Change to created http response
    @PostMapping("/create_user")
    public ResponseEntity<User> create(@RequestBody CreateUserRequest createUserRequest) {
        User user = userService.create(createUserRequest);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> get(@PathVariable Long id, PersistentEntityResourceAssembler assembler) {
        User user = this.userService.findById(id);
        return ResponseEntity.ok(assembler.toFullResource(user));
    }

    @PreAuthorize("isOwnProfile(#id)")
    @PutMapping("/users/{id}")
    public ResponseEntity<?> put(@PathVariable("id") Long id, @RequestBody UpdateUserRequest updateUserRequest, PersistentEntityResourceAssembler assembler) {
        User user = this.userService.update(id, updateUserRequest);
        return ResponseEntity.ok(assembler.toFullResource(user));
    }


    @PostMapping("/create_role")
    public ResponseEntity<Role> createRole(@RequestBody String roleName) {
        Roles roleEnum = Roles.valueOf(roleName);
        Role role = userService.createRole(roleEnum);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/add_role/{userId}/{roleName}")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        Roles roleEnum = Roles.valueOf(roleName);
        userService.addRoleToUser(userId, roleEnum);
        return ResponseEntity.ok("Role added to user successfully");
    }




}
