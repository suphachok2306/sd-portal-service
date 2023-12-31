package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pcc.portalservice.model.enums.StatusApprove;
import java.util.Date;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Status")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Status {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private StatusApprove status;

  private int active;

  @Temporal(TemporalType.DATE)
  private Date approvalDate;

  @ManyToOne
  @JoinColumn(name = "approve_id")
  private User approveId;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "training_id")
  private Training training;

  private int indexOfSignature;
}
