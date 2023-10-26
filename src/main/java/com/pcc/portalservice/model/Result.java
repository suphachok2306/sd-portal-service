package com.pcc.portalservice.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Result")
public class Result {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String result1;
  private String result2;
  private String result3;
  private String result4;
  private String result5;
  private String result6;
  private String result7;
  private String result;
  private String comment;
  private String cause;
  private String plan;

  @Temporal(TemporalType.DATE)
  private Date evaluationDate;

  @ManyToOne
  @JoinColumn(name = "training_id")
  @JsonIgnore
  private Training training;

//  @ManyToOne
//  @JoinColumn(name = "evaluator_id")
//  @JsonIgnore
//  private User evaluator;
}
