package com.pcc.portalservice.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ResultGeneric9")
public class ResultGeneric9 {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String result1;
  private String result2;
  private String result3;
  private String result4;
  private String result5;

  @ManyToOne
  @JoinColumn(name = "training_id")
  @JsonIgnore
  private Training training;
}
