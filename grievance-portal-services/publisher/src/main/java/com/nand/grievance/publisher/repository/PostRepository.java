package com.nand.grievance.publisher.repository;

import com.nand.grievance.publisher.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    PostEntity findByPostId(String postId);

    List<PostEntity> findAllByTicketId(Long ticketId);

    List<PostEntity> findAllByParentPostId(String postId);

    List<PostEntity> findAllByIsResolved(Integer isResolved);

    @Query(name = "resolvedPostCount", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.isResolved=1 and p.ParentPostID is null", nativeQuery = true)
    Long getResolvedPostCount();

    @Query(name = "unResolvedPostCount", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.isResolved=0 and p.ParentPostID is null", nativeQuery = true)
    Long getUnResolvedPostCount();

    @Query(name = "escalatedPostCount", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.isEscalated=1 and p.ParentPostID is null", nativeQuery = true)
    Long getEscalatedPostCount();

    @Query(name = "totalPostCount", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.ParentPostID is null", nativeQuery = true)
    Long getTotalPostCount();

    @Query(name = "totalAnonymousPostCount", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p " +
            "where p.UserID = :userId " +
            "and p.ParentPostID is null", nativeQuery = true)
    Long getTotalAnonymousPostCount(@Param("userId") Long userId);

    @Query(name = "totalUnResolvedAnonymousPostCount", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p " +
            "where p.UserID = :userId " +
            "and p.isResolved=0 " +
            "and p.ParentPostID is null", nativeQuery = true)
    Long getTotalUnResolvedAnonymousPostCount(@Param("userId") Long userId);

    @Query(name = "totalUnResolvedAnonymousPostCount", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p " +
            "where p.UserID = :userId " +
            "and p.isResolved=1 " +
            "and p.ParentPostID is null", nativeQuery = true)
    Long getTotalResolvedAnonymousPostCount(@Param("userId") Long userId);


    //-------------Date range
    @Query(name = "resolvedPostCountByDateRange", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.isResolved=1 and p.ParentPostID is null and p.DateCreated between (:startDate) and (:endDate)", nativeQuery = true)
    Long getResolvedPostCountByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(name = "unResolvedPostCountByDateRange", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.isResolved=0 and p.ParentPostID is null and p.DateCreated between (:startDate) and (:endDate)", nativeQuery = true)
    Long getUnResolvedPostCountByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(name = "escalatedPostCountByDateRange", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.isEscalated=1 and p.ParentPostID is null and p.DateCreated between (:startDate) and (:endDate)", nativeQuery = true)
    Long getEscalatedPostCountByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(name = "totalPostCountByDateRange", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p where p.ParentPostID is null and p.DateCreated between (:startDate) and (:endDate)", nativeQuery = true)
    Long getTotalPostCountByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(name = "totalAnonymousPostCountByDateRange", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p " +
            "where p.UserID = :userId " +
            "and p.ParentPostID is null and p.DateCreated between (:startDate) and (:endDate)", nativeQuery = true)
    Long getTotalAnonymousPostCountByDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(name = "totalUnResolvedAnonymousPostCountByDateRange", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p " +
            "where p.UserID = :userId " +
            "and p.isResolved=0 " +
            "and p.ParentPostID is null and p.DateCreated between (:startDate) and (:endDate)", nativeQuery = true)
    Long getTotalUnResolvedAnonymousPostCountByDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(name = "totalUnResolvedAnonymousPostCountByDateRange", value = "select count(1) from GREVPORTAL.dbo.GRVPubPost p " +
            "where p.UserID = :userId " +
            "and p.isResolved=1 " +
            "and p.ParentPostID is null and p.DateCreated between (:startDate) and (:endDate)", nativeQuery = true)
    Long getTotalResolvedAnonymousPostCountByDateRange(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
