package com.pcc.portalservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

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

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;

    @ManyToOne
    @JoinColumn(name = "userEvaluator_id")
    private User evaluator;
}
