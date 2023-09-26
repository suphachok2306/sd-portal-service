package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@BasePathAwareController
public class UserController {

    private final UserService userService;


    @GetMapping("/findUserById")
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
    public ResponseEntity<Object> createRole(@RequestBody String roleName) {
         Roles roleEnum = Roles.valueOf(roleName);
        
        try {
            Role role = userService.createRole(roleEnum);
            if (role == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Role นี้มีอยู่ในระบบอยู่แล้ว " + roleName);
            }
            return ResponseEntity.ok(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid role name: " + roleName);
        }
    }




    @PostMapping("/addRoleToUser")
    public ResponseEntity<?> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
        Roles roleEnum = Roles.valueOf(roleName);
        userService.addRoleToUser(userId, roleEnum);
        return ResponseEntity.ok("Role added to user successfully");
    }

    @DeleteMapping("deleteById")
    public Object delete(@RequestParam Long id) {
        return userService.deleteData(id);
    }

    @GetMapping("/findAllEmployee")
    public ResponseEntity<List<User>> getAllEmployee() {
        List<User> users = userService.findAllEmployee();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findAllPersonnel")
    public ResponseEntity<List<User>> getAllPersonnel() {
        List<User> users = userService.findAllPersonnel();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findAllVicePresident")
    public ResponseEntity<List<User>> getAllVicePresident() {
        List<User> users = userService.findAllVicePresident();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findAllApprover")
    public ResponseEntity<List<User>> getAllApprover() {
        List<User> users = userService.findAllApprover();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/findAllAdmin")
    public ResponseEntity<List<User>> getAllAdmin() {
        List<User> users = userService.findAllAdmin();
        return ResponseEntity.ok(users);
    }





}
