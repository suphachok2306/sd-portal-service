package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COMPANY")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String companyName;

  @JsonIgnore
  @OneToOne(mappedBy = "company")
  private Sector sector;

  @JsonIgnore
  @OneToOne(mappedBy = "company")
  private Budget budget;
}
