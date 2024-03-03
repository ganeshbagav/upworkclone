package com.incubator.upwork.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.Admin;

public interface AdminRepository extends JpaRepository<Admin,Integer>{
    
}
