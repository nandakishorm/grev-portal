package com.nand.grievance.publisher.controller;

import com.nand.grievance.publisher.dto.FileDTO;
import com.nand.grievance.publisher.dto.FileIdListDTO;
import com.nand.grievance.publisher.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/image")
class FileUploadController {

    @Autowired
    private FileService fileService;

    // Set the maximum allowed file size (1 MB)
    private static final long MAX_FILE_SIZE = 1 * 1024 * 1024;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<String> handleFileUpload(
            @RequestPart("file") MultipartFile file,
            @RequestParam(name = "postId") String postId
    ) throws Exception {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file provided", HttpStatus.BAD_REQUEST);
        }

        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            return new ResponseEntity<>("File size exceeds the maximum allowed size (1 MB)", HttpStatus.BAD_REQUEST);
        }

        try {
            //Capturing upload status just in case if needed for an external system
            Boolean uploadStatus = fileService.uploadFile(file, postId);

            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error uploading the file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/download/{imageId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) {
        try {
            FileDTO fileDTO = fileService.findByImageId(imageId);
            ByteArrayResource resource = new ByteArrayResource(fileDTO.getFileObj());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg")
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentLength(fileDTO.getFileObj().length)
                    .body(resource);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/post/{postId}/imageids")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<FileIdListDTO> getAllFileIdsByPostId(
            @PathVariable String postId
    ) throws Exception {
        FileIdListDTO fileIdListDTO = fileService.findAllByPostId(postId);
        return new ResponseEntity<>(fileIdListDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteImageById(@PathVariable Long imageId) throws Exception {
        Boolean status = fileService.deleteImageById(imageId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Boolean> deleteAllImageByPostId(@PathVariable String postId) throws Exception {
        Boolean status = fileService.deleteAllImageByPostId(postId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
