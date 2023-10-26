package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

  // private String title;

  @ManyToMany(fetch = FetchType.EAGER)
  private Set<Role> roles = new HashSet<>();

  @ToString.Exclude//ใส่มาแก้ loop .hashcode()
  @OneToOne
  @JoinColumn(name = "sector_id")
  private Sector sector;

  
  @OneToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @OneToOne
  @JoinColumn(name = "position_id")
  private Position position;

  @OneToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToOne(mappedBy = "user")
  @JsonIgnore
  private Training training;

  @OneToOne(mappedBy = "user")
  @JsonIgnore
  private Signature signature;


  @Override
  public int hashCode() {
      return Objects.hash(id, empCode, firstname, lastname, email, roles);
  }

}
