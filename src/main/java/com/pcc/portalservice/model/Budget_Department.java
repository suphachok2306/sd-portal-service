// package com.pcc.portalservice.model;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import java.math.BigDecimal;
// import javax.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Table(name = "Budget_Department")
// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class Budget_Department {

//   @Id
//   @GeneratedValue(strategy = GenerationType.IDENTITY)
//   private Long id;

//   private String year;
//   private BigDecimal total_exp;
//   private String type;

//   @ManyToOne(optional = false)
//   @JoinColumn(name = "company_id")
//   @JsonIgnore
//   private Company company;

//   @ManyToOne(optional = false)
//   @JoinColumn(name = "department_id")
//   @JsonIgnore
//   private Department department;
// }
