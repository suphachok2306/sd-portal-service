package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DEPARTMENT")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String deptName;
  private String deptCode;

  @OneToMany(mappedBy = "department")
  @JsonIgnore
  private List<Position> positions = new ArrayList<>();

  @OneToOne(mappedBy = "department")
  @JsonIgnore
  private User user;

  @ManyToOne
  @JoinColumn(name = "sector_id")
  @JsonIgnore
  private Sector sector;

}
