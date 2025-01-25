//package com.hm.grievance.publisher.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//
//@Entity
//@Table(name = "GRVPubRole")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class RoleEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Integer id;
//
//    @Column(name = "roleid")
//    private Integer roleId;
//
//    @Column(name = "rolename")
//    private String roleName;
//
//    @Column(name = "active")
//    private Integer active;
//
//    @Column(name = "datemodified")
//    private Date dateModified;
//
//    @Column(name = "usermodified")
//    private String userModified;
//
//}