package com.pcc.portalservice.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    @Temporal(TemporalType.DATE)
    private Date dateSave;

    @Builder.Default
    private int day = 0;

    private float budget;

    private String action;

    @JsonIgnore
    private String active;

    @Temporal(TemporalType.DATE)
    private Date actionDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    private List<Course> courses = new ArrayList<>();
    
    @ToString.Exclude//ใส่มาแก้ loop .hashcode()
    @ManyToOne
    @JoinColumn(name = "approve1_id")
    private User approve1;


    @OneToMany(mappedBy = "training")
    private List<Status> status = new ArrayList<>();

    @OneToMany(mappedBy = "training")
    private List<Result> result = new ArrayList<>();
}
