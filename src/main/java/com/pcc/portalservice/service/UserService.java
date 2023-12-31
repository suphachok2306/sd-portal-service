package com.pcc.portalservice.service;

import com.pcc.portalservice.model.*;
import com.pcc.portalservice.model.enums.Roles;
import com.pcc.portalservice.model.enums.StatusUser;
import com.pcc.portalservice.repository.*;
import com.pcc.portalservice.requests.CreateEmployeeRequest;
import com.pcc.portalservice.requests.CreateUserRequest;
import com.pcc.portalservice.requests.EditEmployeeRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nonapi.io.github.classgraph.concurrency.SingletonMap.NewInstanceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

  private final EntityManager entityManager;

  public User create(CreateUserRequest createUserRequest) {
    if (
      userRepository.existsByEmail(createUserRequest.getEmail()) &&
      (
        createUserRequest.getEmail() != null &&
        !createUserRequest.getEmail().isEmpty()
      )
    ) {
      throw new RuntimeException("Email is already in use.");
    }

    String hashedPassword = authenticationService.hashPassword(
      createUserRequest.getPassword()
    );

    User user = User
      .builder()
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
      if (userRepository.existsByEmpCode(empCode)) {
        throw new RuntimeException("EmpCode is already in use.");
      }
    } else {
      if (
        (userRepository.existsByEmail(email)) &&
        userRepository.existsByEmpCode(empCode)
      ) {
        throw new RuntimeException(
          "Both Email and EmpCode are already in use."
        );
      } else {
        if (userRepository.existsByEmail(email)) {
          throw new RuntimeException("Email is already in use.");
        }
        if (userRepository.existsByEmpCode(empCode)) {
          throw new RuntimeException("EmpCode is already in use.");
        }
      }
    }

    Optional<Department> departmentOptional = departmentRepository.findById(
      createEmployeeRequest.getDeptID().get(0)
    );

    Department department = departmentOptional.orElseThrow(() ->
      new RuntimeException("Department not found / DeptCode or DeptName wrong")
    );

    Optional<Position> positionOptional = positionRepository.findByPositionNameAndDepartment(
      createEmployeeRequest.getPositionName(),
      department
    );

    Position position = positionOptional.orElseThrow(() ->
      new RuntimeException("Position not found")
    );

    Department departmentA = departmentRepository.findById(createEmployeeRequest.getDept_actual()).orElseThrow(() ->
            new RuntimeException("Department not found")
    );

    Sector sectorA = sectorRepository.findById(createEmployeeRequest.getSector_actual()).orElseThrow(() ->
            new RuntimeException("Sector not found")
    );


    User user = User
      .builder()
      .companys(new HashSet<>())
      .sectors(new HashSet<>())
      .departments(new HashSet<>())
      .empCode(createEmployeeRequest.getEmpCode())
      .title(createEmployeeRequest.getTitle())
      .firstname(createEmployeeRequest.getFirstname())
      .lastname(createEmployeeRequest.getLastname())
      .email(createEmployeeRequest.getEmail())
            .department(departmentA)
            .sector(sectorA)
      .roles(new HashSet<>())
      .position(position)
      .status(StatusUser.เป็นพนักงานอยู่.toString())
      .build();

    for (String roleName : createEmployeeRequest.getRoles()) {
      Roles roleEnum = null;
      try {
        roleEnum = Roles.valueOf(roleName);
      } catch (IllegalArgumentException e) {
        roleEnum = Roles.User;
      }

      Role role = roleRepository.findByRole(roleEnum).orElse(null);
      if (role != null && !user.getRoles().contains(role)) {
        user.getRoles().add(role);
      }
    }

    for (Long deptID : createEmployeeRequest.getDeptID()) {
      Department department_id = departmentRepository
        .findById(deptID)
        .orElse(null);
      if (deptID != null) {
        user.getDepartments().add(department_id);
      }
    }

    for (Long sectorID : createEmployeeRequest.getSectorID()) {
      Sector sector_id = sectorRepository.findById(sectorID).orElse(null);
      if (sectorID != null) {
        user.getSectors().add(sector_id);
      }
    }

    for (Long companyID : createEmployeeRequest.getCompanyID()) {
      Company company_id = companyRepository.findById(companyID).orElse(null);
      if (companyID != null) {
        user.getCompanys().add(company_id);
      }
    }

    return userRepository.save(user);
  }

  public User editUser(Long id, EditEmployeeRequest editEmployeeRequest) {
    String email = editEmployeeRequest.getEmail();
    String empCode = editEmployeeRequest.getEmpCode();
    User user = userRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("UserId not found: "));

    if (
      (email == null || email.isEmpty()) &&
      userRepository.existsByEmpCode(empCode) &&
      !user.getEmpCode().equals(empCode)
    ) {
      throw new RuntimeException("EmpCode is already in use.");
    } else if (
      userRepository.existsByEmail(email) &&
      !user.getEmail().equals(email) &&
      userRepository.existsByEmpCode(empCode) &&
      !user.getEmpCode().equals(empCode)
    ) {
      throw new RuntimeException("Both Email and EmpCode are already in use.");
    } else if (
      userRepository.existsByEmail(email) && !user.getEmail().equals(email)
    ) {
      throw new RuntimeException("Email is already in use.");
    } else if (
      userRepository.existsByEmpCode(empCode) &&
      !user.getEmpCode().equals(empCode)
    ) {
      throw new RuntimeException("EmpCode is already in use.");
    }

    Department departmentA = departmentRepository.findById(editEmployeeRequest.getDept_actual()).orElseThrow(() ->
            new RuntimeException("Department not found")
    );

    Sector sectorA = sectorRepository.findById(editEmployeeRequest.getSector_actual()).orElseThrow(() ->
            new RuntimeException("Sector not found")
    );

    user.setEmpCode(editEmployeeRequest.getEmpCode());
    user.setTitle(editEmployeeRequest.getTitle());
    user.setFirstname(editEmployeeRequest.getFirstname());
    user.setLastname(editEmployeeRequest.getLastname());
    user.setEmail(editEmployeeRequest.getEmail());
    user.setSector(sectorA);
    user.setDepartment(departmentA);
    user.setSectors(new HashSet<>());
    user.setCompanys(new HashSet<>());
    user.setDepartments(new HashSet<>());
    user.setRoles(new HashSet<>());

    userRepository.save(user);

    boolean positionFound = false;

    for (Long i : editEmployeeRequest.getDeptID()) {

        Department department_id = departmentRepository
          .findById(i)
          .orElse(null);

        if (department_id != null) {
          user.getDepartments().add(department_id);

          Optional<Position> positionOptional = positionRepository.findByPositionNameAndDepartment(
            editEmployeeRequest.getPositionName(),
            department_id
          );

          Position position = positionOptional.orElse(null);

          if (position != null) {
            user.setPosition(position);
            positionFound = true;

        }
      }
    }

   if (!positionFound) {
     throw new RuntimeException("Position not found in any department");
   }

    for (Long sectorID : editEmployeeRequest.getSectorID()) {
      Sector sector_id = sectorRepository.findById(sectorID).orElse(null);
      if (sector_id != null) {
        user.getSectors().add(sector_id);
      }
    }

    for (Long companyID : editEmployeeRequest.getCompanyID()) {
      Company company_id = companyRepository.findById(companyID).orElse(null);
      if (company_id != null) {
        user.getCompanys().add(company_id);
      }
    }

    for (String RoleString : editEmployeeRequest.getRoles()) {
      Roles roleEnum = null;
      try {
        roleEnum = Roles.valueOf(RoleString);
      } catch (IllegalArgumentException e) {
        throw e;
      }

      Role role = roleRepository.findByRole(roleEnum).orElse(null);
      if (role != null && !user.getRoles().contains(role)) {
        user.getRoles().add(role);
      }
    }

    return userRepository.save(user);
  }

  public Role createRole(Roles roleName) {
    if (!roleRepository.existsByRole(roleName)) {
      Role role = Role.builder().role(roleName).build();
      return roleRepository.save(role);
    } else {
      return null;
    }
  }

  public String deleteData(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    if (optionalUser.isPresent()) {
      userRepository.deleteById(id);
      return "ลบข้อมูลของ ID : " + id + " เรียบร้อย";
    } else {
      return null;
    }
  }

  public boolean isEmpNull(CreateEmployeeRequest request) {
    return (
      request == null ||
      request.getEmpCode() == null ||
      request.getEmpCode().isEmpty() ||
      request.getFirstname() == null ||
      request.getFirstname().isEmpty() ||
      request.getLastname() == null ||
      request.getLastname().isEmpty()
    );
  }

  public boolean isEditEmpNull(EditEmployeeRequest request) {
    return (
      request == null ||
      request.getFirstname() == null ||
      request.getFirstname().isEmpty() ||
      request.getLastname() == null ||
      request.getLastname().isEmpty()
    );
  }

  public boolean isUserNull(CreateUserRequest request) {
    return (
      request == null ||
      request.getFirstname() == null ||
      request.getFirstname().isEmpty() ||
      request.getLastname() == null ||
      request.getLastname().isEmpty() ||
      request.getTelephone() == null ||
      request.getTelephone().isEmpty()
    );
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

  public List<User> findByDept() {
    return null;
  }

  public List<User> findActiveEmployees() {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> query = builder.createQuery(User.class);
    Root<User> root = query.from(User.class);
    query.where(builder.equal(root.get("status"), "เป็นพนักงานอยู่"));

    List<User> users = entityManager.createQuery(query).getResultList();

    return users;
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

  public User findByEmail(String email) {
    return userRepository
      .findByEmail(email)
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found")
      );
  }

  public Object searchUser(
    String empCode,
    String name,
    String position,
    String email,
    String deptName,
    String deptCode,
    String company,
    String sectorName,
    String sectorCode
  ) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> query = builder.createQuery(User.class);
    Root<User> root = query.from(User.class);

    List<Predicate> predicates = new ArrayList<>();

    if (empCode != null) {
      predicates.add(
        builder.like(
          builder.lower(root.get("empCode")),
          "%" + empCode.toLowerCase() + "%"
        )
      );
    }

    if (name != null) {
      Expression<String> fullName = builder.concat(
        builder.concat(builder.lower(root.get("firstname")), " "),
        builder.lower(root.get("lastname"))
      );
      predicates.add(
        builder.like(builder.lower(fullName), "%" + name.toLowerCase() + "%")
      );
    }

    if (email != null) {
      predicates.add(
        builder.like(
          builder.lower(root.get("email")),
          "%" + email.toLowerCase() + "%"
        )
      );
    }

    if (position != null) {
      Join<User, Position> positionJoin = root.join("position");
      predicates.add(
        builder.like(
          builder.lower(positionJoin.get("positionName")),
          "%" + position.toLowerCase() + "%"
        )
      );
    }

    if (deptName != null || deptCode != null) {
      Join<User, Department> departmentJoin = root.join("departments");

      if (deptName != null) {
        predicates.add(
          builder.like(
            builder.lower(departmentJoin.get("deptName")),
            "%" + deptName.toLowerCase() + "%"
          )
        );
      }

      if (deptCode != null) {
        predicates.add(
          builder.like(
            builder.lower(departmentJoin.get("deptCode")),
            "%" + deptCode.toLowerCase() + "%"
          )
        );
      }
    }

    if (company != null) {
      Join<User, Company> companyJoin = root.join("companys");
      predicates.add(
        builder.like(
          builder.lower(companyJoin.get("companyName")),
          "%" + company.toLowerCase() + "%"
        )
      );
    }

    if (sectorName != null || sectorCode != null) {
      Join<User, Sector> sectorJoin = root.join("sectors");

      if (sectorName != null) {
        predicates.add(
          builder.like(
            builder.lower(sectorJoin.get("sectorName")),
            "%" + sectorName.toLowerCase() + "%"
          )
        );
      }

      if (sectorCode != null) {
        predicates.add(
          builder.like(
            builder.lower(sectorJoin.get("sectorCode")),
            "%" + sectorCode.toLowerCase() + "%"
          )
        );
      }
    }

    if (predicates.isEmpty()) {
      return "ไม่พบรายการที่ต้องการค้นหา";
    }

    query.where(predicates.toArray(new Predicate[0]));

    List<User> users = entityManager.createQuery(query).getResultList();

    if (users.isEmpty()) {
      return "ไม่พบรายการที่ต้องการค้นหา";
    }

    return users;
  }

  public User setStatusToUser(Long User_id, StatusUser statusUser) {
    User user = userRepository
      .findById(User_id)
      .orElseThrow(() ->
        new RuntimeException("User not found with ID: " + User_id)
      );

    user.setStatus(statusUser.toString());
    return userRepository.save(user);
  }

  public void addRoleToUser(Long userId, Roles roleName) {
    User user = userRepository
      .findById(userId)
      .orElseThrow(() -> new RuntimeException("UserId not found: " + userId));
    Role role = roleRepository
      .findByRole(roleName)
      .orElseThrow(() ->
        new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          "Role not found: " + roleName
        )
      );

    user.getRoles().add(role);
    userRepository.save(user);
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
        } else {
          Role roleEntity = new Role();
          roleEntity.setRole(role);
          roleRepository.delete(roleEntity);
        }
      }
    }
  }
}
