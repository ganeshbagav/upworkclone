package com.incubator.upwork.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incubator.upwork.data.model.Freelancer;
import com.incubator.upwork.data.model.JobPost;
import com.incubator.upwork.data.model.JobProposals;

public interface JobProposalsRepository extends JpaRepository<JobProposals,Integer>{
    @Query(value = "SELECT COUNT(*) FROM jobproposals jp WHERE jp.freelancer = :freelancer", nativeQuery = true)
    public Integer getJobPostProposalCount(String freelancer);
    
    public List<JobProposals> findAllByFreelancer(Freelancer freelancer);

    // @Query(value = "SELECT * FROM jobproposals jp WHERE jp.freelancer = :freelancer AND jobpost = :jobpost", nativeQuery = true)
    public List<JobProposals> findByFreelancerAndJobpost(Freelancer freelancer , JobPost jobPost);
}
