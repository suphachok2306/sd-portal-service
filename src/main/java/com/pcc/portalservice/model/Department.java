package com.pcc.portalservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    //@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "department")
    private List<Position> positions = new ArrayList<>();

    @OneToOne(mappedBy = "department")
    private User user;
}

