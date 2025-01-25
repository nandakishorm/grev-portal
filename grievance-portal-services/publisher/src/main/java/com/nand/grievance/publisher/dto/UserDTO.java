package com.nand.grievance.publisher.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {

    private Integer id;

    private String userId;

    private String roleName;

    private String username;

    @JsonIgnore
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private Integer active;

    private Date dateCreated;

    private String userCreated;

    private Date dateModified;

    private String userModified;

}
