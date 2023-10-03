package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Null;


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

}
