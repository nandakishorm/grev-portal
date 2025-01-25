package com.nand.grievance.publisher.service;

import com.nand.grievance.publisher.constant.RoleEnum;
import com.nand.grievance.publisher.dto.UserDTO;
import com.nand.grievance.publisher.entity.UserEntity;
import com.nand.grievance.publisher.repository.UserRepository;
import com.nand.grievance.publisher.util.AppUtil;
import com.nand.grievance.publisher.util.ModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    public String addUser(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoleName(RoleEnum.USER.value());
        userEntity.setUserId(AppUtil.generateRandomUUID());
        userEntity.setUserCreated(userEntity.getUsername());
        userEntity.setDateCreated(new Date());
        userEntity.setUserModified(userEntity.getUsername());
        userEntity.setDateModified(new Date());

        userRepository.save(userEntity);

        log.info("New user added to the system, Username: " + userEntity.getUsername());
        return "New user added to the system";
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntityList = userRepository.findByActive(1).orElse(Collections.emptyList());
        return modelMapperUtil.convertToDto(userEntityList, UserDTO.class);
    }
}
