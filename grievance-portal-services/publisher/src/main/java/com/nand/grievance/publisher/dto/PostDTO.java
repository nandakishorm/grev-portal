package com.nand.grievance.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDTO {

    private Integer id;

    private String userId;

    private String postId;

    private String parentPostId;

    private Long ticketId;

    private String categoryId;

    private String post;

    private Integer upVotesCount;

    private Integer isResolved;

    private Integer isEscalated;

    private Integer active;

    private Date dateCreated;

    private String userCreated;

    private Date dateModified;

    private String userModified;

}
