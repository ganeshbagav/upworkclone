package com.incubator.upwork.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.MileStone;
import com.incubator.upwork.data.model.Project;

public interface MileStoneRepository extends JpaRepository<MileStone,Integer>{
    
    List<MileStone> findAllByProject(Project project);
}
