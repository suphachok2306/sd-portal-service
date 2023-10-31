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
  private float budgetTraining;
  private float budgetCer;
  private float total_exp;

  @ManyToOne(optional = false)
  @JoinColumn(name = "company_id")
  private Company company;

  @ManyToOne(optional = false)
  @JoinColumn(name = "department_id")
  private Department department;
}
