package com.pcc.portalservice.model;

import lombok.*;

import javax.persistence.*;
import java.util.*;

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

  @Temporal(TemporalType.DATE)
  private Date actionDate;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToMany
  private List<Course> courses = new ArrayList<>();

  @ToString.Exclude //ใส่มาแก้ loop .hashcode()
  @ManyToOne
  @JoinColumn(name = "approve1_id")
  private User approve1;

//  @ToString.Exclude //ใส่มาแก้ loop .hashcode()
//  @ManyToOne
//  @JoinColumn(name = "approve2_id")
//  private User approve2;
//
//  @ToString.Exclude //ใส่มาแก้ loop .hashcode()
//  @ManyToOne
//  @JoinColumn(name = "approve3_id")
//  private User approve3;
//
//  @ToString.Exclude //ใส่มาแก้ loop .hashcode()
//  @ManyToOne
//  @JoinColumn(name = "approve4_id")
//  private User approve4;

  @OneToMany(mappedBy = "training")
  private List<Status> status = new ArrayList<>();

  @OneToMany(mappedBy = "training")
  private List<Result> result = new ArrayList<>();

//  @ManyToMany
//  @JoinTable(
//          name = "training_signature",
//          joinColumns = @JoinColumn(name = "user_id"),
//          inverseJoinColumns = @JoinColumn(name = "signature_id")
//  )
//  private Set<User> users = new HashSet<>();

//  @ManyToMany
//  private List<User> users = new ArrayList<>();


}
