package com.nand.grievance.publisher.service;

import com.nand.grievance.publisher.constant.RoleEnum;
import com.nand.grievance.publisher.dto.MessageDTO;
import com.nand.grievance.publisher.dto.PostDTO;
import com.nand.grievance.publisher.dto.PostMetricsDTO;
import com.nand.grievance.publisher.entity.CategoryEntity;
import com.nand.grievance.publisher.entity.PostEntity;
import com.nand.grievance.publisher.entity.UserEntity;
import com.nand.grievance.publisher.exception.ServiceException;
import com.nand.grievance.publisher.repository.CategoryRepository;
import com.nand.grievance.publisher.repository.PostRepository;
import com.nand.grievance.publisher.repository.UserRepository;
import com.nand.grievance.publisher.util.AppUtil;
import com.nand.grievance.publisher.util.EmailUtil;
import com.nand.grievance.publisher.util.ModelMapperUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService extends GenericService<PostEntity> {

    public static final String ANONYMOUS_USERNAME = "anonymous";
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private EscalationService escalationService;

    @SneakyThrows
    public PostDTO createPost(PostDTO postDTO, Boolean isSendEmail)  {
        postDTO.setPostId(AppUtil.generateRandomUUID());
        if (postDTO.getParentPostId() == null || postDTO.getParentPostId().isEmpty()) {
            postDTO.setTicketId(AppUtil.generate8DigitRandomNumber());
        }
        postDTO.setIsResolved(0);
        postDTO.setIsEscalated(0);
        PostEntity postEntity = modelMapperUtil.convertToDto(postDTO, PostEntity.class);
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(postDTO.getCategoryId());
        postEntity.setCategoryEntity(categoryEntity);
        UserEntity userEntity = getAutheticatedUserEntity();
        postEntity.setUserEntity(userEntity);
        setAuditInfo(postEntity, userEntity, true);

        postEntity = postRepository.save(postEntity);

        if (isSendEmail) {
            sendEmail(postEntity);
        }

        postDTO = modelMapperUtil.convertToDto(postEntity, PostDTO.class);
        log.info("New post created, PostId: " + postDTO.getPostId());
        return postDTO;
    }

    public PostDTO updatePost(PostDTO postDTO) {
        PostEntity originalPostEntity = postRepository.findByPostId(postDTO.getPostId());
        originalPostEntity.setPost(postDTO.getPost());
        originalPostEntity.setUpVotesCount(postDTO.getUpVotesCount());
        originalPostEntity.setIsEscalated(postDTO.getIsEscalated());
        originalPostEntity.setIsResolved(postDTO.getIsResolved());
        originalPostEntity.setDateModified(postDTO.getDateModified());
        originalPostEntity.setUserModified(postDTO.getUserModified());

        UserEntity userEntity = getAutheticatedUserEntity();
        originalPostEntity.setUserModified(userEntity.getUsername());

        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(postDTO.getCategoryId());
        originalPostEntity.setCategoryEntity(categoryEntity);

        setAuditInfo(originalPostEntity, userEntity, false);

        PostEntity updatedPostEntity = postRepository.save(originalPostEntity);
        postDTO = modelMapperUtil.convertToDto(updatedPostEntity, PostDTO.class);

        log.info("Post updated, PostId: " + postDTO.getPostId());
        return postDTO;
    }

    public PostDTO getPostById(String postId) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        return modelMapperUtil.convertToDto(postEntity, PostDTO.class);
    }

    @SneakyThrows
    public List<MessageDTO> getPostThreadByPostId(String postId) {
        List<PostEntity> postEntityList = new ArrayList<>();
        postEntityList.add(postRepository.findByPostId(postId));
        if (postEntityList.isEmpty()) {
            throw new ServiceException("No parent post found for the post id");
        }

        List<PostEntity> parentPostEntityList = postRepository.findAllByParentPostId(postId);
        postEntityList.addAll(parentPostEntityList);

        List<PostDTO> postDTOList = modelMapperUtil.convertToDto(postEntityList, PostDTO.class);
        return getMessageDTOListHierarchically(postDTOList);
    }

    public List<PostDTO> getPostsByTicketNumber(Long ticketNumber) {
        List<PostEntity> postEntityList = postRepository.findAllByTicketId(ticketNumber);
        return modelMapperUtil.convertToDto(postEntityList, PostDTO.class);
    }

    public List<PostDTO> getAllPosts() throws Exception {
        List<PostEntity> postEntityList = postRepository.findAll();
        return modelMapperUtil.convertToDto(postEntityList, PostDTO.class);
    }

    public List<MessageDTO> getAllPostsHierarchically(Boolean unResolvedPosts) throws Exception {
        List<PostEntity> postEntityList;
        if (unResolvedPosts) {
            postEntityList = postRepository.findAllByIsResolved(0);
        } else {
            postEntityList = postRepository.findAll();
        }

        List<PostDTO> postDTOList = modelMapperUtil.convertToDto(postEntityList, PostDTO.class);

        return getMessageDTOListHierarchically(postDTOList);
    }

    public Boolean deletePostById(String postId) throws Exception {
        try {
            PostEntity postEntity = postRepository.findByPostId(postId);
            postRepository.delete(postEntity);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw e;
        }

        log.info("Post deleted, PostId: " + postId);
        return Boolean.TRUE;
    }

    public PostDTO upVotePost(String postId) {
        PostEntity postEntity = postRepository.findByPostId(postId);
        postEntity.setUpVotesCount(postEntity.getUpVotesCount() + 1);
        postRepository.save(postEntity);
        return modelMapperUtil.convertToDto(postEntity, PostDTO.class);
    }

    @SneakyThrows
    public PostDTO resolvePost(String postId) {

        //Validation - Only the person who created the post or Admin can resolve the post
        UserEntity userEntity = getAutheticatedUserEntity();
        PostEntity postEntity = postRepository.findByPostId(postId);
        if (!userEntity.getUserId().equals(postEntity.getUserEntity().getUserId())) {
            userRepository.findAllByRoleNameAndUserId(RoleEnum.ADMIN.value(), userEntity.getUserId())
                    .orElseThrow(() -> new ServiceException("Apologies! Only the user who raised the " +
                            "grievance or admins can mark the post as resolved!!"));
        }

        //Validation-2 - Only a parent post can be marked as resolved
        if (postEntity.getParentPostId() != null && !postEntity.getParentPostId().isEmpty()) {
            throw new ServiceException("Apologies! Only a parent post can be marked as resolved");
        }

        postEntity.setIsResolved(1);
        postEntity.setActive(0);
        postRepository.save(postEntity);

        List<PostEntity> postEntityList = postRepository.findAllByParentPostId(postId);
        postEntityList.forEach(childPostEntity -> {
            childPostEntity.setIsResolved(1);
            childPostEntity.setActive(0);
            postRepository.save(childPostEntity);
        });

        log.info("Post resolved, PostId: " + postId);
        return modelMapperUtil.convertToDto(postEntity, PostDTO.class);
    }

    public PostMetricsDTO getPostMetrics(Date startDate, Date endDate) {
        //TODO: Give a metrics for the graphs -
        // Number of posts opened,
        // Number of posts resolved,
        // Number of posts escalated,
        // Number of posts which are highly active (take a threshold value to filter active posts)

        PostMetricsDTO postMetricsDTO = new PostMetricsDTO();

        if (startDate != null && endDate != null ) {
            postMetricsDTO.setResolvedPostCount(postRepository.getResolvedPostCountByDateRange(startDate, endDate));
            postMetricsDTO.setUnResolvedPostCount(postRepository.getUnResolvedPostCountByDateRange(startDate, endDate));
            postMetricsDTO.setEscalatedPostCount(postRepository.getEscalatedPostCountByDateRange(startDate, endDate));
            postMetricsDTO.setTrendingPostCount(null);
            postMetricsDTO.setTotalPostCount(postRepository.getTotalPostCountByDateRange(startDate, endDate));

            Long totalAnonymousPostCount = userRepository.findByUsername(ANONYMOUS_USERNAME).map(entity -> postRepository.getTotalAnonymousPostCountByDateRange(entity.getId(), startDate, endDate)).orElse(0L);
            postMetricsDTO.setTotalAnonymousPostCount(totalAnonymousPostCount);

            Long totalUnResolvedAnonymousPostCount = userRepository.findByUsername(ANONYMOUS_USERNAME).map(entity -> postRepository.getTotalUnResolvedAnonymousPostCountByDateRange(entity.getId(), startDate, endDate)).orElse(0L);
            postMetricsDTO.setTotalUnResolvedAnonymousPostCount(totalUnResolvedAnonymousPostCount);

            Long totalResolvedAnonymousPostCount = userRepository.findByUsername(ANONYMOUS_USERNAME).map(entity -> postRepository.getTotalResolvedAnonymousPostCountByDateRange(entity.getId(), startDate, endDate)).orElse(0L);
            postMetricsDTO.setTotalResolvedAnonymousPostCount(totalResolvedAnonymousPostCount);
        } else {
            postMetricsDTO.setResolvedPostCount(postRepository.getResolvedPostCount());
            postMetricsDTO.setUnResolvedPostCount(postRepository.getUnResolvedPostCount());
            postMetricsDTO.setEscalatedPostCount(postRepository.getEscalatedPostCount());
            postMetricsDTO.setTrendingPostCount(null);
            postMetricsDTO.setTotalPostCount(postRepository.getTotalPostCount());

            Long totalAnonymousPostCount = userRepository.findByUsername(ANONYMOUS_USERNAME).map(entity -> postRepository.getTotalAnonymousPostCount(entity.getId())).orElse(0L);
            postMetricsDTO.setTotalAnonymousPostCount(totalAnonymousPostCount);

            Long totalUnResolvedAnonymousPostCount = userRepository.findByUsername(ANONYMOUS_USERNAME).map(entity -> postRepository.getTotalUnResolvedAnonymousPostCount(entity.getId())).orElse(0L);
            postMetricsDTO.setTotalUnResolvedAnonymousPostCount(totalUnResolvedAnonymousPostCount);

            Long totalResolvedAnonymousPostCount = userRepository.findByUsername(ANONYMOUS_USERNAME).map(entity -> postRepository.getTotalResolvedAnonymousPostCount(entity.getId())).orElse(0L);
            postMetricsDTO.setTotalResolvedAnonymousPostCount(totalResolvedAnonymousPostCount);
        }

        return postMetricsDTO;
    }

    private static List<MessageDTO> getMessageDTOListHierarchically(List<PostDTO> postDTOList) {
        Map<String, MessageDTO> messageMap = postDTOList.stream().filter(postDTO -> postDTO.getParentPostId() == null)
                .map(postDTO -> new MessageDTO(postDTO.getPostId(), postDTO, new ArrayList<>()))
                .toList().stream()
                .collect(Collectors.toMap(MessageDTO::getPostId, messageDTO -> messageDTO));

        /*messageMap.forEach((key, message) -> {
            postDTOList.stream().filter(postDTO -> postDTO.getParentPostId().equals(key))
                    .forEach(postDTO -> message.getResponses().add(postDTO));
        });*/

        for (PostDTO postDTO: postDTOList) {
            if (postDTO.getParentPostId() != null && messageMap.containsKey(postDTO.getParentPostId())) {
                messageMap.get(postDTO.getParentPostId()).getResponses().add(postDTO);
            }
        }

        List<MessageDTO> messageDTOList = new ArrayList<>();
        messageMap.forEach((postId, messageDTO) -> messageDTOList.add(messageDTO));
        return messageDTOList;
    }

    private void setAuditInfo(PostEntity entity, UserEntity userEntity, Boolean isNew) {
        if (isNew) {
            entity.setUserCreated(userEntity.getUsername());
            entity.setDateCreated(new Date());
        }
        entity.setUserModified(userEntity.getUsername());
        entity.setDateModified(new Date());
    }

    private Boolean sendEmail(PostEntity postEntity) {
        /*try {
            StringBuilder stringBuilder = new StringBuilder("Please find the message posted below, " + "\n" + postEntity.getPost());
            UserEntity userEntity = getAutheticatedUserEntity();
            if (postEntity.getParentPostId() == null || postEntity.getParentPostId().isBlank()) {
                emailUtil.sendEmail(userEntity.getEmail(), "Grievance Portal | Your Query has been posted",
                        stringBuilder.toString());
            } else {
                emailUtil.sendEmail(postRepository.findByPostId(
                                postEntity.getParentPostId()).getUserEntity().getEmail(),
                        "Grievance Portal | Someone responded to your query",
                        stringBuilder.toString());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }*/

        /* Custom impl to handle Ascend level restrictions on this POC */
        List<String> emailList = new ArrayList<>();
        emailList.add("nandakishor.m@ascendlearning.com");
        emailList.add("sangeetha.gopalappa@ascendlearning.com");
        emailList.add("hafeez.farooq@ascendlearning.com");
        emailList.add("faizan.shariff@ascendlearning.com");

        if (!emailList.contains(postEntity.getUserEntity().getEmail())) {
            log.warn("External email found, hence skipping email notification for post = " + postEntity.getPostId());
            return false;
        }

        try {
            StringBuilder stringBuilder = new StringBuilder("Please find the message posted below, " + "\n" + postEntity.getPost());
            UserEntity userEntity = getAutheticatedUserEntity();
            if (postEntity.getParentPostId() == null || postEntity.getParentPostId().isBlank()) {
                // 'New Message posted' email
                emailUtil.sendEmail(userEntity.getEmail(), "Test Mail", stringBuilder.toString());
            } else {
                // 'Someone-replied to the message' email
                emailUtil.sendEmail(postRepository.findByPostId(postEntity.getParentPostId()).getUserEntity().getEmail(),
                        "Test subject", stringBuilder.toString());
            }
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @SneakyThrows
    public Boolean escalatePost(String postId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException("Apologies!! No user found for the email id provided!!"));

        PostEntity postEntity = postRepository.findByPostId(postId);
        if (postEntity == null) {
            throw new ServiceException("Apologies!! No Post found for the post-id provided!!");
        }
        postEntity.setIsEscalated(1);
        postRepository.save(postEntity);

        escalationService.escalatePost(postId, userEntity.getUserId());
        return true;
    }
}
