package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Null;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "USERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String empCode;
    
    private String firstname;
    private String lastname;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String telephone;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Signature signature;

    @OneToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;


    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Training training;


//    @OneToOne(optional = false)
//    @JoinColumn(name = "signature_id")
//    @JsonIgnore
//    private Signature signature;
    //มันแดงเลยคอมเม้น

}
