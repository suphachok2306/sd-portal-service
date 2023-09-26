package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long result_id;
    private String result_1;
    private String result_2;
    private String result_3;
    private String result_4;
    private String result_5;
    private String result_6;
    private String result_7;

    @OneToOne
    @JoinColumn(name = "training_id")
    @JsonIgnore
    private Training training;
}
