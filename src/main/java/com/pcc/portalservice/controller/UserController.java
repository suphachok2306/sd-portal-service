package com.pcc.portalservice.controller;

import com.pcc.portalservice.model.Role;
import com.pcc.portalservice.model.User;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.EditEmployeeRequest;
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
    public ResponseEntity<User> findUserById(@RequestParam Long userId) {
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

    @PutMapping("/editEmployee")
    public ResponseEntity<User> editEmployee(
            @RequestParam Long userId,
            @RequestBody EditEmployeeRequest editEmployeeRequest
    ) {
        User editUser = userService.editUser(userId, editEmployeeRequest);
        if (editUser != null) {
            return new ResponseEntity<>(editUser, HttpStatus.OK);
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
    public ResponseEntity<?> addRoleToUser(@RequestParam Long userId, @RequestParam String roleName) {
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
