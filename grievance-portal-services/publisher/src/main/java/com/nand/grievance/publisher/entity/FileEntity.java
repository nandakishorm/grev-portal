package com.nand.grievance.publisher.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "GRVPubPostImage")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "postid", nullable = false)
    private String postId;

    @Lob
    @Column(name = "fileobj", columnDefinition = "varbinary(max) NOT NULL" )
    private byte[] fileObj;

    @Column(name = "active", columnDefinition = "int default 1")
    private Integer active;

    @Column(name = "datecreated")
    private Date dateCreated;

    @Column(name = "usercreated")
    private String userCreated;

    @Column(name = "datemodified")
    private Date dateModified;

    @Column(name = "usermodified")
    private String userModified;

    public FileEntity(byte[] fileObj) {
        this.fileObj = fileObj;
    }

}
