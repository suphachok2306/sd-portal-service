package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SECTOR")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sector {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String sectorName;
  private String sectorCode;

  
  @JsonIgnore
  @ManyToMany(mappedBy = "sector")
  private List<Department> departments = new ArrayList<>();

  @ManyToMany(mappedBy = "sectors")
  @JsonIgnore
  private List<User> users = new ArrayList<>();


  @ManyToOne
  @JoinColumn(name = "company_id")
  @JsonIgnore
  private Company company;

  @Override
  public int hashCode() {
    return Objects.hash(id, sectorName, sectorCode);
  }
}