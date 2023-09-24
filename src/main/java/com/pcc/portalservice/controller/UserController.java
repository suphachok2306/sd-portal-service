package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.UpdateUserRequest;
import com.pcc.portalservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class UserController {

    private final UserService userService;


    @GetMapping("/findUserById") // เปลี่ยนเส้นทางตามความเหมาะสม
    public ResponseEntity<User> findUserById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> create(@RequestBody CreateUserRequest createUserRequest) {
        User user = userService.create(createUserRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/createEmployee")
    public ResponseEntity<User> create(@RequestBody CreateEmployeeRequest createEmployeeRequest) {
        User user = userService.createEmployee(createEmployeeRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/editUser")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @RequestBody CreateEmployeeRequest createEmployeeRequest
    ) {
        User updatedUser = userService.editUser(userId, createEmployeeRequest);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/createRole")
    public ResponseEntity<Role> createRole(@RequestBody String roleName) {
        Roles roleEnum = Roles.valueOf(roleName);
        Role role = userService.createRole(roleEnum);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/addRoleToUser")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        Roles roleEnum = Roles.valueOf(roleName);
        userService.addRoleToUser(userId, roleEnum);
        return ResponseEntity.ok("Role added to user successfully");
    }

    @GetMapping("/findAllEmployee")
    public ResponseEntity<List<User>> getUsersWithUserRole() {
        List<User> users = userService.findAllEmployee();
        return ResponseEntity.ok(users);
    }

}
