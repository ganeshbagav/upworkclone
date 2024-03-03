package com.incubator.upwork.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.Industries;

public interface IndustriesRepository extends JpaRepository<Industries,Integer>{
    
}
