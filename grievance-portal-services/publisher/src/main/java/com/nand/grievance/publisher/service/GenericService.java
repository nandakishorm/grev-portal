package com.nand.grievance.publisher.service;

import com.nand.grievance.publisher.entity.UserEntity;
import com.nand.grievance.publisher.exception.ServiceException;
import com.nand.grievance.publisher.repository.UserRepository;
import com.nand.grievance.publisher.util.EmailUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.function.ThrowingSupplier;

@Component
public class GenericService<B> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailUtil emailUtil;

    @SneakyThrows
    protected UserEntity getAutheticatedUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Retrieve the JWT token from the Authentication object
        String username = (String) authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow((ThrowingSupplier<Throwable>) () -> new ServiceException("Unable to obtain the user account details for the logged-in user!!"));
    }

//    protected void setAuditInfo(B entity, UserEntity userEntity, Boolean isNew) {
//        if (isNew) {
//            entity.setUserCreated(userEntity.getUsername());
//            entity.setDateCreated(new Date());
//        }
//        entity.setUserModified(userEntity.getUsername());
//        entity.setDateModified(new Date());
//    }

}
