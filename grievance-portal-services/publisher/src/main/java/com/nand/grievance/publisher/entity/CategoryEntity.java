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

import java.util.Date;

@Entity
@Table(name = "GRVPubCategory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "categoryid")
    private String categoryId;

    @Column(name = "categoryname")
    private String categoryName;

    @Column(name = "categorytype")
    private String categoryType;

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
