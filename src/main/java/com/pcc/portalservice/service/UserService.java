package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
//import com.pcc.portalservice.specs.UserSpecs;
import com.pcc.portalservice.requests.EditEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    // Services
    private final AuthenticationService authenticationService;
    // Repositories
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final SectorRepository sectorRepository;
    private final CompanyRepository companyRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }



    public User create(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new RuntimeException("Email is already in use.");
        }
        String hashedPassword = authenticationService.hashPassword(createUserRequest.getPassword());

        User user = User.builder()
                .email(createUserRequest.getEmail())
                .firstname(createUserRequest.getFirstname())
                .lastname(createUserRequest.getLastname())
                .password(hashedPassword)
                .telephone(createUserRequest.getTelephone())
                .roles(new ArrayList<>())
                .build();

        for (String roleName : createUserRequest.getRoles()) {
            Roles roleEnum = null;
            try {
                roleEnum = Roles.valueOf(roleName); // ตรวจสอบว่ามีใน enum Roles หรือไม่
            } catch (IllegalArgumentException e) {
                // ถ้าไม่มีใน enum Roles ให้กำหนดให้เป็น "User"
                roleEnum = Roles.User;
            }

            Role role = roleRepository.findByRole(roleEnum).orElse(null);
            if (role != null) {
                user.getRoles().add(role);
            }
        }

        return userRepository.save(user);
    }


    public User createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        if (userRepository.existsByEmail(createEmployeeRequest.getEmail())) {
            throw new RuntimeException("Email is already in use.");
        }

        Company companyName = companyRepository.findByCompanyName(createEmployeeRequest.getCompanyName())
                .orElseThrow(() -> new RuntimeException("companyName not found: " + createEmployeeRequest.getCompanyName()));

        Sector sectorName = sectorRepository.findBySectorName(createEmployeeRequest.getSectorName())
                .orElseThrow(() -> new RuntimeException("sectorName not found: " + createEmployeeRequest.getSectorName()));

        Sector sectorCode = sectorRepository.findBySectorCode(createEmployeeRequest.getSectorCode())
                .orElseThrow(() -> new RuntimeException("sectorCode not found: " + createEmployeeRequest.getSectorCode()));

        Department departmentName = departmentRepository.findByDeptName(createEmployeeRequest.getDeptName())
                .orElseThrow(() -> new RuntimeException("departmentName not found: " + createEmployeeRequest.getDeptName()));

        Department departmentCode = departmentRepository.findByDeptCode(createEmployeeRequest.getDeptCode())
                .orElseThrow(() -> new RuntimeException("departmentCode not found: " + createEmployeeRequest.getDeptCode()));

        Position positionName = positionRepository.findByPositionName(createEmployeeRequest.getPositionName())
                .orElseThrow(() -> new RuntimeException("positionName not found: " + createEmployeeRequest.getPositionName()));

        User user = User.builder()
                .company(companyName)
                .sector(sectorCode)
                .sector(sectorName)
                .department(departmentCode)
                .department(departmentName)
                .empCode(createEmployeeRequest.getEmpCode())
                .firstname(createEmployeeRequest.getFirstname())
                .lastname(createEmployeeRequest.getLastname())
                .email(createEmployeeRequest.getEmail())
                .roles(new ArrayList<>())
                .position(positionName)
                .build();

        for (String roleName : createEmployeeRequest.getRoles()) {
            Roles roleEnum = null;
            try {
                roleEnum = Roles.valueOf(roleName); // ตรวจสอบว่ามีใน enum Roles หรือไม่
            } catch (IllegalArgumentException e) {
                // ถ้าไม่มีใน enum Roles ให้กำหนดให้เป็น "User"
                roleEnum = Roles.User;
            }

            Role role = roleRepository.findByRole(roleEnum).orElse(null);
            if (role != null) {
                user.getRoles().add(role);
            }
        }

        return userRepository.save(user);
    }

    public User editUser(Long id, EditEmployeeRequest editEmployeeRequest) {
        User user = findById(id);
        Position position = positionRepository.findByPositionName(editEmployeeRequest.getPositionName())
                .orElseThrow(() -> new RuntimeException("Position not found: " + editEmployeeRequest.getPositionName()));

        user.setEmail(editEmployeeRequest.getEmail());
        user.setFirstname(editEmployeeRequest.getFirstname());
        user.setLastname(editEmployeeRequest.getLastname());
        user.setPosition(position);
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

    public String deleteData(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        }
        return null;
    }

    public List<User> findAllEmployee() {
        return userRepository.findByRolesRole(Roles.User);
    }
    public List<User> findAllPersonnel() {
        return userRepository.findByRolesRole(Roles.Personnel);
    }
    public List<User> findAllVicePresident() {
        return userRepository.findByRolesRole(Roles.VicePresident);
    }
    public List<User> findAllApprover() {
        return userRepository.findByRolesRole(Roles.Approver);
    }
    public List<User> findAllAdmin() {
        return userRepository.findByRolesRole(Roles.Admin);
    }


}

