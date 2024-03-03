package com.incubator.upwork.data.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incubator.upwork.data.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(value = "SELECT COUNT(*) FROM project pr WHERE  pr.clientid = :clientid AND iscomplete = 1", nativeQuery = true)
    public Integer getClientCompleteProjectCount(String clientid);

    @Query(value = "SELECT COUNT(*) FROM project pr WHERE  pr.clientid = :clientid AND iscomplete = 0", nativeQuery = true)
    public Integer getClientIncompleteProjectCount(String clientid);

    @Query(value = "SELECT COUNT(*) FROM project pr WHERE pr.freelancerid = :freelancerid AND pr.iscomplete = 1", nativeQuery = true)
    public Integer getFreelancerCompleteProjectCount(String freelancerid);

    @Query(value = "SELECT COUNT(*) FROM project pr WHERE pr.freelancerid = :freelancerid AND pr.iscomplete = 0", nativeQuery = true)
    public Integer getFreelancerIncompleteProjectCount(String freelancerid);

    @Query(value = "SELECT * FROM project pr WHERE  pr.clientid = :clientid AND iscomplete = 1", nativeQuery = true)
    public List<Project> getClientCompleteProject(String clientid);

    @Query(value = "SELECT * FROM project pr WHERE  pr.clientid = :clientid AND iscomplete = 0", nativeQuery = true)
    public List<Project> getClientIncompleteProject(String clientid);

    @Query(value = "SELECT * FROM project pr WHERE pr.freelancerid = :freelancerid AND pr.iscomplete = 1", nativeQuery = true)
    public List<Project> getFreelancerCompleteProject(String freelancerid);

    @Query(value = "SELECT * FROM project pr WHERE pr.freelancerid = :freelancerid AND pr.iscomplete = 0", nativeQuery = true)
    public List<Project> getFreelancerIncompleteProject(String freelancerid);

    public List<Project> findAllByClientid(int clientid);

}
