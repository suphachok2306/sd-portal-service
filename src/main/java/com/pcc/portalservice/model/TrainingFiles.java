package com.pcc.portalservice.model;

import static javax.persistence.GenerationType.IDENTITY;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainingFiles")
public class TrainingFiles {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @JsonIgnore
  private String fileType;
  @JsonIgnore
  private String filePath;
  private String fileName;

  @ManyToMany(mappedBy = "trainingFiles")
  @JsonIgnore
  private List<Training> training = new ArrayList<>();
  
}
