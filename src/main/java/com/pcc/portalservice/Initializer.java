//package com.pcc.portalservice;
//
//import com.pcc.portalservice.model.Role;
//import com.pcc.portalservice.model.User;
//import com.pcc.portalservice.model.enums.Roles;
//import com.pcc.portalservice.repository.RoleRepository;
//import com.pcc.portalservice.requests.CreateUserRequest;
//import com.pcc.portalservice.service.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@AllArgsConstructor
//@Component
//public class Initializer implements CommandLineRunner {
//
//    private final UserService userService;
//
//    private final RoleRepository roleRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        //////////สร้าง Role
//        Role adminRole = createRole(Roles.ADMIN);
//        Role approverRole = createRole(Roles.APPROVER);
//        Role userRole = createRole(Roles.USER);
//
//
//
//        //////////สร้าง User
//        List<Roles> adminR = Collections.singletonList(Roles.ADMIN);
//        List<Roles> approverR = Collections.singletonList(Roles.APPROVER);
//        List<Roles> userR = Collections.singletonList(Roles.USER);
//
//        //User adminTest = createUser("admin", "admin", "ainmad","191" ,"1234", "admin@pccth.com",adminR);
//        //User approverTest = createUser("approver", "approver", "approver","191" ,"1234", "approver@pccth.com",approverR);
//        //User userTest = createUser("user", "user", "user","191" ,"1234", "user@pccth.com",userR);
//
//
//    }
//
//    private User createUser(String username, String firstname, String lastname, String telephone, String password, String email, List<Roles> roles) {
//        CreateUserRequest createUserRequest = CreateUserRequest.builder()
//                .username(username)
//                .firstname(firstname)
//                .lastname(lastname)
//                .password(password)
//                .email(email)
//                .telephone(telephone)
//                .roles(roles.stream().map(Roles::name).collect(Collectors.toList()))
//                .build();
//        return this.userService.create(createUserRequest);
//    }
//
//
//
//
//    private Role createRole(Roles role) {
//        Role r = new Role();
//        r.setRole(role);
//        return roleRepository.save(r);
//    }
//}
