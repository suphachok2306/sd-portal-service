// package com.pcc.portalservice.model;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// import javax.persistence.*;
// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Table(name = "SIGNATURE")
// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class Signature{
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;
//     private byte[] image;


//     @OneToOne(mappedBy = "signature")
//     @JsonIgnore
//     private User user;
// }