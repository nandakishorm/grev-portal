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
public class FileDTO {

    private Long id;

    private String postId;

    private byte[] fileObj;

    private Integer active;

    private Date dateCreated;

    private String userCreated;

    private Date dateModified;

    private String userModified;

}
