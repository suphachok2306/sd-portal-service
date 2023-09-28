package com.pcc.portalservice.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Training")
public class Training {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Timestamp dateSave;
    private int day;
    private String action;
    private Timestamp actionDate;
    private String approve1;
    private String approve2;
    private String approve3;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    private Collection<Course> courses = new ArrayList<>();

}
