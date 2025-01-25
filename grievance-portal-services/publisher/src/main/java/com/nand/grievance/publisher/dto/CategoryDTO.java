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
public class CategoryDTO {

    private Integer id;

    private String categoryId;

    private String categoryName;

    private String categoryType;

    private Integer active;

    private Date dateCreated;

    private String userCreated;

    private Date dateModified;

    private String userModified;

}
