package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BUDGET")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String year;
  private String class_name;
  private String remark;
  private int numberOfPerson;
  private float fee;
  private float airAcc;
  private float exp;

  @ManyToOne
  @JoinColumn(name = "sector_id")
  private Sector sector;

  @ManyToOne(optional = false)
  @JoinColumn(name = "company_id")
  @JsonIgnore
  private Company company;

  @ManyToOne(optional = false)
  @JoinColumn(name = "department_id")
  @JsonIgnore
  private Department department;
}
