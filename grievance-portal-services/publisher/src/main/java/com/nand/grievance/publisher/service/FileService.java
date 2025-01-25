package com.nand.grievance.publisher.service;

import com.nand.grievance.publisher.constant.RoleEnum;
import com.nand.grievance.publisher.dto.FileDTO;
import com.nand.grievance.publisher.dto.FileIdListDTO;
import com.nand.grievance.publisher.entity.FileEntity;
import com.nand.grievance.publisher.entity.PostEntity;
import com.nand.grievance.publisher.entity.UserEntity;
import com.nand.grievance.publisher.exception.ServiceException;
import com.nand.grievance.publisher.repository.FileRepository;
import com.nand.grievance.publisher.repository.PostRepository;
import com.nand.grievance.publisher.repository.UserRepository;
import com.nand.grievance.publisher.util.ModelMapperUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileService extends GenericService<FileEntity> {

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    protected void setAuditInfo(FileEntity entity, UserEntity userEntity, Boolean isNew) {
        if (isNew) {
            entity.setUserCreated(userEntity.getUsername());
            entity.setDateCreated(new Date());
        }
        entity.setUserModified(userEntity.getUsername());
        entity.setDateModified(new Date());
    }

    @SneakyThrows(value = { IOException.class, RuntimeException.class })
    public Boolean uploadFile(MultipartFile file, String postId) throws IOException {
        // Save the file to the database
        FileEntity fileEntity = new FileEntity(file.getBytes());
        fileEntity.setPostId(postId);
        fileEntity.setActive(1);
        setAuditInfo(fileEntity, getAutheticatedUserEntity(), true);
        fileRepository.save(fileEntity);
        return true;
    }

    @SneakyThrows
    public FileDTO findByImageId(Long imageId) {
        Optional<FileEntity> imageOptional = fileRepository.findById(imageId);
        /*return imageOptional
                .map(imageEntity -> new ByteArrayResource(imageEntity.getImageObj()))
                .orElse(null);*/

        return imageOptional
                .map(fileEntity -> modelMapperUtil.convertToDto(fileEntity, FileDTO.class))
                .orElse(null);

    }

    @SneakyThrows
    public FileIdListDTO findAllByPostId(String postId) {
        List<FileEntity> fileEntityList = fileRepository.findAllByPostId(postId);
        List<Long> fileIds = fileEntityList.stream().map(FileEntity::getId).toList();
        FileIdListDTO fileIdListDTO = new FileIdListDTO();
        fileIdListDTO.setFileIds(fileIds);
        return fileIdListDTO;
    }

    @SneakyThrows
    public Boolean deleteImageById(Long id) {

        //Validate the user's authority
        UserEntity userEntity = getAutheticatedUserEntity();
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(() -> new ServiceException("No corresponding to post found to delink and delete the file!"));
        PostEntity postEntity = postRepository.findByPostId(fileEntity.getPostId());
        if (!userEntity.getUserId().equals(postEntity.getUserEntity().getUserId())) {
            userRepository.findAllByRoleNameAndUserId(RoleEnum.ADMIN.value(), userEntity.getUserId())
                    .orElseThrow(() -> new ServiceException("Apologies! Only the user who uploaded the " +
                            "files or admins can delete the files!!"));
        }

        //Everything looks good
        fileRepository.deleteById(id);
        return true;
    }

    @SneakyThrows
    public Boolean deleteAllImageByPostId(String postId) {
        //Validate the user's authority
        UserEntity userEntity = getAutheticatedUserEntity();
        FileEntity fileEntity = fileRepository.findAllByPostId(postId).stream().findFirst()
                .orElseThrow(() -> new ServiceException("No corresponding to post found to delink and delete the files!"));
        PostEntity postEntity = postRepository.findByPostId(fileEntity.getPostId());
        if (!userEntity.getUserId().equals(postEntity.getUserEntity().getUserId())) {
            userRepository.findAllByRoleNameAndUserId(RoleEnum.ADMIN.value(), userEntity.getUserId())
                    .orElseThrow(() -> new ServiceException("Apologies! Only the user who uploaded the " +
                            "files or admins can delete the files!!"));
        }

        List<FileEntity> fileEntityList = fileRepository.findAllByPostId(postId);
        for (FileEntity entity: fileEntityList) {
            fileRepository.deleteById(entity.getId());
        }

        return true;
    }
}
