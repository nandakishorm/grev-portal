package com.nand.grievance.publisher.controller;

import com.nand.grievance.publisher.dto.MessageDTO;
import com.nand.grievance.publisher.dto.PostDTO;
import com.nand.grievance.publisher.dto.PostMetricsDTO;
import com.nand.grievance.publisher.exception.ServiceException;
import com.nand.grievance.publisher.service.PostService;
import com.nand.grievance.publisher.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<PostDTO> createPost (
            @RequestBody PostDTO postDTO,
            @RequestParam(name = "isSendEmail", required = false) Boolean isSendEmail
    ) throws Exception {
        isSendEmail = isSendEmail != null ? isSendEmail : false;
        PostDTO persistedPostDTO = postService.createPost(postDTO, isSendEmail);
        if (persistedPostDTO == null) {
            throw new ServiceException("Unable to create the Post, please try again later");
        } else {
            return new ResponseEntity<>(persistedPostDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<PostDTO>> getAllPosts() throws Exception {
        List<PostDTO> postDTOList = postService.getAllPosts();
        if (postDTOList == null) {
            throw new ServiceException("Unable to retrieve the Post list");
        } else {
            return new ResponseEntity<List<PostDTO>>(postDTOList, HttpStatus.OK);
        }
    }

    @GetMapping("/all/hierarchical")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<MessageDTO>> getAllPostsHierarchically(
            @RequestParam(name = "unResolvedPosts") Boolean unResolvedPosts
    ) throws Exception {
        List<MessageDTO> messageDTOList = postService.getAllPostsHierarchically(unResolvedPosts);
        if (messageDTOList == null) {
            throw new ServiceException("Unable to retrieve the Post list");
        } else {
            return new ResponseEntity<>(messageDTOList, HttpStatus.OK);
        }
    }

    @GetMapping("/{postId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<PostDTO> getPostById(
            @PathVariable(name = "postId") String postId
    ) throws Exception {
        PostDTO postDTO = postService.getPostById(postId);
        if (postDTO == null)
            throw new ServiceException("No Post found for the id provided");
        else
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
    }

    @GetMapping("/{postId}/thread")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<MessageDTO>> getPostThreadById(
            @PathVariable(name = "postId") String postId
    ) throws Exception {
        List<MessageDTO> messageDTOList = postService.getPostThreadByPostId(postId);
        if (messageDTOList == null)
            throw new ServiceException("No Post found for the id provided");
        else
            return new ResponseEntity<>(messageDTOList, HttpStatus.OK);
    }

    @GetMapping("/ticket/{ticketNumber}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<PostDTO>> getPostsByTicketNumber(
            @PathVariable(name = "ticketNumber") Long ticketNumber
    ) throws Exception {
        List<PostDTO> postDTOList = postService.getPostsByTicketNumber(ticketNumber);
        if (postDTOList == null)
            throw new ServiceException("No Post found for the ticket number provided");
        else
            return new ResponseEntity<>(postDTOList, HttpStatus.OK);
    }

    @PutMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<PostDTO> updatePost (
            @RequestBody PostDTO postDTO
    ) throws Exception {
        PostDTO persistedPostDTO = postService.updatePost(postDTO);
        if (persistedPostDTO == null) {
            throw new ServiceException("Unable to update the Post, please try again later");
        } else {
            return new ResponseEntity<>(persistedPostDTO, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<String> deletePost(
            @PathVariable(name = "postId") String postId
    ) throws Exception {
        Boolean isDeleted = postService.deletePostById(postId);
        if (!isDeleted) {
            throw new ServiceException("Unable to delete the Post for the provided id = " + postId);
        } else {
            return new ResponseEntity<>("Successfully deleted the Post for the provided id = " + postId, HttpStatus.OK);
        }
    }

    @PutMapping("/{postId}/upvote")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<PostDTO> upVotePost(
            @PathVariable(name = "postId") String postId
    ) throws Exception {
        PostDTO postDTO = postService.upVotePost(postId);
        if (postDTO == null) {
            throw new ServiceException("Unable to Upvote the post for the post id = " + postId);
        } else {
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
        }
    }

    @PutMapping("/{postId}/markresolve")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<PostDTO> resolvePost(
            @PathVariable(name = "postId") String postId
    ) throws Exception {
        PostDTO postDTO = postService.resolvePost(postId);
        if (postDTO == null) {
            throw new ServiceException("Unable to mark the post as resolved, please try again later");
        } else {
            return new ResponseEntity<>(postDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/all/metrics")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<PostMetricsDTO> getPostMetrics(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate
    ) throws Exception {
        Date start = null;
        Date end = null;

        if (startDate == null && endDate != null) {
            throw new ServiceException("Please provide both Date ranges");
        } else if(startDate != null && endDate == null) {
            throw new ServiceException("Please provide both Date ranges");
        } else if (startDate != null) {
            try {
                start = AppUtil.convertStringToDate(startDate, "yyyy-MM-dd");
                end = AppUtil.convertStringToDate(endDate, "yyyy-MM-dd");
            } catch (ParseException e) {
                throw new ServiceException("Please provide Dates in this format: yyyy-MM-dd");
            }
        }

        PostMetricsDTO dto = postService.getPostMetrics(start, end);
        if (dto == null) {
            throw new ServiceException("Unable to fetch the Metrics values, please try again later");
        } else {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
    }

    @PostMapping("/{postId}/escalate")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Boolean> escalatePost(
            @PathVariable(name = "postId") String postId,
            @RequestParam(name = "email", required = true) String email
    ) throws Exception {
        Boolean isEscalated = postService.escalatePost(postId, email);

        if (isEscalated == null) {
            throw new ServiceException("Unable to escalate, please try again later");
        } else {
            return new ResponseEntity<>(isEscalated, HttpStatus.OK);
        }
    }

    //TODO: Add a method to remove/resolve escalation

}
