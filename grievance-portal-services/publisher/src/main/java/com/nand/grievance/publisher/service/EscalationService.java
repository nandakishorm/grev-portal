package com.nand.grievance.publisher.service;

import com.nand.grievance.publisher.entity.EscalationEntity;
import com.nand.grievance.publisher.entity.UserEntity;
import com.nand.grievance.publisher.exception.ServiceException;
import com.nand.grievance.publisher.repository.EscalationRepository;
import com.nand.grievance.publisher.repository.UserRepository;
import com.nand.grievance.publisher.util.ModelMapperUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.function.ThrowingSupplier;

import java.util.Date;

@Slf4j
@Service
public class EscalationService {

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Autowired
    private EscalationRepository repository;

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    public void escalatePost(String userId, String postId) {
        EscalationEntity entity = new EscalationEntity();
        entity.setUserId(userId);
        entity.setPostId(postId);
        entity.setActive(1);
        setAuditInfo(entity, getAutheticatedUserEntity(), true);

        repository.save(entity);
    }

    //TODO: Add a method to get list of escalated posts

    private void setAuditInfo(EscalationEntity entity, UserEntity userEntity, Boolean isNew) {
        if (isNew) {
            entity.setUserCreated(userEntity.getUsername());
            entity.setDateCreated(new Date());
        }
        entity.setUserModified(userEntity.getUsername());
        entity.setDateModified(new Date());
    }

    @SneakyThrows
    private UserEntity getAutheticatedUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Retrieve the JWT token from the Authentication object
        String username = (String) authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow((ThrowingSupplier<Throwable>) () -> new ServiceException("Unable to obtain the user account details for the logged-in user!!"));
    }
}
