package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SECTOR")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sectorName;
    private String sectorCode;

    @JsonIgnore
    @OneToMany(mappedBy = "sector")
    private List<Department> departments = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "sector")
    private User user;
}
