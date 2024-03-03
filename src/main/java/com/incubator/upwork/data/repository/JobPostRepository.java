package com.incubator.upwork.data.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;

import com.incubator.upwork.data.model.Client;
import com.incubator.upwork.data.model.JobPost;

public interface JobPostRepository extends JpaRepository<JobPost,Integer>{
    
    @Query(value = "SELECT COUNT(*) FROM jobpost jp WHERE jp.client = :client AND jp.hide = :hide", nativeQuery = true)
    public Integer getJobPostCount(String client,Integer hide); 

    public List<JobPost> findAllByClient(Client client , Sort sort );
    public List<JobPost> findAllByClientAndHide(Client client ,Integer hide, Sort sort );
}

// @Query(value = "SELECT * FROM BookmarkList bm WHERE bm.bookmarked_by_user_mobile_number = :mobileNumber",nativeQuery = true)
// public List<BookmarkList> findBookmarkPost(String mobileNumber );