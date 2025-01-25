package com.nand.grievance.publisher.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "GRVPubEscalate")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EscalationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "postid")
    private String postId;

    @Column(name = "userid")
    private String userId;

    @Column(name = "active")
    private Integer active;

    @Column(name = "datecreated")
    private Date dateCreated;

    @Column(name = "usercreated")
    private String userCreated;

    @Column(name = "datemodified")
    private Date dateModified;

    @Column(name = "usermodified")
    private String userModified;

}
