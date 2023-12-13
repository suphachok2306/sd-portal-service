package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.*;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "USERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String empCode;

  private String firstname;
  private String lastname;
  private String email;

  @JsonIgnore
  private String password;

  @JsonIgnore
  private String telephone;

  private String status;
  private String title;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles = new HashSet<>();

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Sector> sectors = new HashSet<>();

  @ManyToMany
  @JoinTable(name = "user_departments", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "department_id"))
  private Set<Department> departments = new HashSet<>();

  @ManyToMany
  private Set<Company> companys = new HashSet<>();

  @OneToOne
  @JoinColumn(name = "position_id")
  private Position position;

  @OneToOne(mappedBy = "user")
  @JsonIgnore
  private Training training;

  @OneToOne(mappedBy = "user")
  @JsonIgnore
  private Signature signature;

  @OneToOne
  @JoinColumn(name = "dept_actual")
  private Department department;

  @OneToOne
  @JoinColumn(name = "sector_actual")
  private Sector sector;

  @Override
  public int hashCode() {
    return Objects.hash(id, empCode, firstname, lastname, email, roles);
  }
}
