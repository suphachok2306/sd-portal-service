package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.EditEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
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


    public User create(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail()) && (createUserRequest.getEmail() != null && !createUserRequest.getEmail().isEmpty())) {
            throw new RuntimeException("Email is already in use.");
        }
    
        String hashedPassword = authenticationService.hashPassword(createUserRequest.getPassword());
    
        User user = User.builder()
                .email(createUserRequest.getEmail())
                .firstname(createUserRequest.getFirstname())
                .lastname(createUserRequest.getLastname())
                .password(hashedPassword)
                .telephone(createUserRequest.getTelephone())
                .roles(new HashSet<>())
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
            if (role != null && !user.getRoles().contains(role)) {
                user.getRoles().add(role);
            }
        }
    
        return userRepository.save(user);
    }
    

    public User createEmployee(CreateEmployeeRequest createEmployeeRequest) {
        String email = createEmployeeRequest.getEmail();
        String empCode = createEmployeeRequest.getEmpCode();


        if (email == null || email.isEmpty() || email.equals("")) {
            if (userRepository.existsByempCode(empCode)) {
                    throw new RuntimeException("EmpCode is already in use.");  
            }
            
        }else{
            if ((userRepository.existsByEmail(email)) && userRepository.existsByempCode(empCode)) {
                throw new RuntimeException("Both Email and EmpCode are already in use.");
            }
            else{
                if (userRepository.existsByEmail(email)) {
                    throw new RuntimeException("Email is already in use.");  
                }
                if (userRepository.existsByempCode(empCode)) {
                throw new RuntimeException("EmpCode is already in use.");  
                }
            }
        }
         
        Company companyName = companyRepository.findByCompanyName(createEmployeeRequest.getCompanyName())
                .orElseThrow(() -> new RuntimeException("companyName not found: " + createEmployeeRequest.getCompanyName()));
    
        Optional<Sector> sectorOptional = sectorRepository.findBySectorCodeAndSectorNameAndCompanyCompanyName(
                createEmployeeRequest.getSectorCode(),
                createEmployeeRequest.getSectorName(),
                createEmployeeRequest.getCompanyName()
        );
    
        Sector sector = sectorOptional.orElseThrow(() -> new RuntimeException("Sector not found / SectorCode or SectorName wrong"));
    
        Optional<Department> departmentOptional = departmentRepository.findByDeptCodeAndDeptName(
                createEmployeeRequest.getDeptCode(),
                createEmployeeRequest.getDeptName()
        );
    
        Department department = departmentOptional.orElseThrow(() -> new RuntimeException("Department not found / DeptCode or DeptName wrong"));
    
        Optional<Position> positionOptional = positionRepository.findByPositionNameAndDepartment(createEmployeeRequest.getPositionName(), department);
    
        Position position = positionOptional.orElseThrow(() -> new RuntimeException("Position not found"));
    
        User user = User.builder()
                .company(companyName)
                .sector(sector)
                .department(department)
                .empCode(createEmployeeRequest.getEmpCode())
                .firstname(createEmployeeRequest.getFirstname())
                .lastname(createEmployeeRequest.getLastname())
                .email(createEmployeeRequest.getEmail())
                .roles(new HashSet<>())
                .position(position)
                .build();
    
                for (String roleName : createEmployeeRequest.getRoles()) {
                    Roles roleEnum = null;
                    try {
                        roleEnum = Roles.valueOf(roleName); // Check if it's a valid role
                    } catch (IllegalArgumentException e) {
                        // If it's not a valid role, set it to "User"
                        roleEnum = Roles.User;
                    }
                
                    Role role = roleRepository.findByRole(roleEnum).orElse(null);
                    if (role != null && !user.getRoles().contains(role)) {
                        user.getRoles().add(role);
                    }
                }
    
        return userRepository.save(user);
    }


    public User editUser(Long id, EditEmployeeRequest editEmployeeRequest) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("UserId not found: " + editEmployeeRequest.getCompanyName()));
        Company companyName = companyRepository.findByCompanyName(editEmployeeRequest.getCompanyName())
                        .orElseThrow(() -> new RuntimeException("companyName not found: " + editEmployeeRequest.getCompanyName()));

        Optional<Sector> sectorOptional = sectorRepository.findBySectorCodeAndSectorNameAndCompanyCompanyName(
            editEmployeeRequest.getSectorCode(),
            editEmployeeRequest.getSectorName(),
            editEmployeeRequest.getCompanyName()
        );

        Sector sector = sectorOptional.orElseThrow(() -> new RuntimeException("Sector not found / SectorCode or SectorName wrong"));

        Optional<Department> departmentOptional = departmentRepository.findByDeptCodeAndDeptName(
            editEmployeeRequest.getDeptCode(),
            editEmployeeRequest.getDeptName()
        );

        Department department = departmentOptional.orElseThrow(() -> new RuntimeException("Department not found / DeptCode or DeptName wrong"));;

        Optional<Position> positionOptional = positionRepository.findByPositionNameAndDepartment(editEmployeeRequest.getPositionName(), department);

        Position position = positionOptional.orElseThrow(() -> new RuntimeException("Position not found"));;

        String email = editEmployeeRequest.getEmail();
        if (userRepository.existsByEmail(email) && (email != null && !email.isEmpty() && !email.equals(user.getEmail()))) {
            throw new RuntimeException("Email is already in use.");  
        }
        else{
            user.setEmail(editEmployeeRequest.getEmail());
        }
        user.setEmpCode(editEmployeeRequest.getEmpCode());
        user.setFirstname(editEmployeeRequest.getFirstname());
        user.setLastname(editEmployeeRequest.getLastname());
        user.setPosition(position);
        user.setSector(sector);
        user.setDepartment(department);
        user.setCompany(companyName);

        return userRepository.save(user);
    }



    public Role createRole(Roles roleName) {
        if (!roleRepository.existsByRole(roleName)) {
            Role role = Role.builder().role(roleName).build();
            return roleRepository.save(role);
        } 
        else {
            return null;
        }
    }
      
      


    public void addRoleToUser(Long userId, Roles roleName) {
       User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("UserId not found: " + userId));
        Role role = roleRepository.findByRole(roleName).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found: " + roleName));

        user.getRoles().add(role);
        userRepository.save(user);
    }


    public String deleteData(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return "ลบข้อมูลเรียบร้อย";
        }
        else{
            return null;
        }
    }


    public boolean isEmpNull(CreateEmployeeRequest request){
        return request == null || request.getEmpCode() == null || request.getEmpCode().isEmpty()
                || request.getFirstname() == null || request.getFirstname().isEmpty()
                || request.getLastname() == null || request.getLastname().isEmpty();
    }

    public boolean isEditEmpNull(EditEmployeeRequest request){
        return request == null || request.getFirstname() == null || request.getFirstname().isEmpty()
                || request.getLastname() == null || request.getLastname().isEmpty();
    }

    public boolean isUserNull(CreateUserRequest request) {
        return request == null || request.getFirstname() == null || request.getFirstname().isEmpty()
                || request.getLastname() == null || request.getLastname().isEmpty()
                || request.getTelephone() == null || request.getTelephone().isEmpty();
    }

    public boolean hasRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return false;
        }
        Optional<Role> role = roleRepository.findByRole(Roles.valueOf(roleName));
        if (role.isEmpty()) {
            return false;
        }
        Role role1 = role.get();
        return user.getRoles().contains(role1);
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
    
    @Component
    public class RoleAutoInserter {

        private final RoleRepository roleRepository;

        public RoleAutoInserter(RoleRepository roleRepository) {
            this.roleRepository = roleRepository;
        }

        @PostConstruct
        public void autoInsertRoles() {
            for (Roles role : Roles.values()) {
                if (!roleRepository.existsByRole(role)) {
                    Role roleEntity = new Role();
                    roleEntity.setRole(role);
                    roleRepository.save(roleEntity);
                }
                else{
                    Role roleEntity = new Role();
                    roleEntity.setRole(role);
                    roleRepository.delete(roleEntity);
                }
            }
        }
    }


}

