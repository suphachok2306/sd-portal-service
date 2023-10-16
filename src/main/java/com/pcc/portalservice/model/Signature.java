package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "SIGNATURE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Signature{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] image;
//    private String image;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}