package com.nand.grievance.publisher.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "GRVPubPost")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @Column(name = "postid", unique = true)
    private String postId;

    @Column(name = "parentpostid")
    private String parentPostId;

    /*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_ticketid_sequence_generator")
    @SequenceGenerator(name = "non_id_column_sequence", sequenceName = "post_ticketid_sequence", allocationSize = 1)*/
    @Column(name = "ticketid")
    private Long ticketId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId")
    private CategoryEntity categoryEntity;

    @Column(name = "post")
    private String post;

    @Column(name = "upvotescount")
    private Integer upVotesCount;

    @Column(name = "isresolved")
    private Integer isResolved;

    @Column(name = "isescalated")
    private Integer isEscalated;

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