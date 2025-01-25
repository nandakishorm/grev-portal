package com.nand.grievance.publisher.controller;

import com.nand.grievance.publisher.dto.UserDTO;
import com.nand.grievance.publisher.entity.UserEntity;
import com.nand.grievance.publisher.exception.ServiceException;
import com.nand.grievance.publisher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/newuser")
    public ResponseEntity<String> addNewUser(@RequestBody UserEntity userEntity) throws Exception {
        if(userEntity.getFirstName() == null)
            throw new ServiceException("Please provide first name");
        else if(userEntity.getLastName() == null)
            throw new ServiceException("Please provide last name");
        else if(userEntity.getUsername() == null)
            throw new ServiceException("Please provide username");
        else if(userEntity.getPassword() == null)
            throw new ServiceException("Please provide password");
        else if(userEntity.getEmail() == null)
            throw new ServiceException("Please provide email");
        else if(userEntity.getPhone() == null)
            throw new ServiceException("Please provide phone number");

        return new ResponseEntity<>(userService.addUser(userEntity), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() throws Exception {
        List<UserDTO> dtos = userService.getAllUsers();
        if (dtos == null)
            throw new ServiceException("No active users found");
        else
            return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
