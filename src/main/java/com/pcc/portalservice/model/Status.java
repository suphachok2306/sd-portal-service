package com.pcc.portalservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pcc.portalservice.model.enums.StatusApprove;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private Long approveId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "training_id")
    private Training training;

}
