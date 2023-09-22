package com.pcc.portalservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private Long user_id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String telephone;
    @ManyToMany(fetch = FetchType.EAGER)
    //@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles", // ชื่อตารางกลาง
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new ArrayList<>();

}
