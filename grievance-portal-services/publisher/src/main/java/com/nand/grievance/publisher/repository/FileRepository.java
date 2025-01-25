package com.nand.grievance.publisher.repository;

import com.nand.grievance.publisher.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findAllByPostId(String postId);

    void deleteAllByPostId(String postId);

}
