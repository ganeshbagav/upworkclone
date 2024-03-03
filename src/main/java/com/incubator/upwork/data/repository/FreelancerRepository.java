package com.incubator.upwork.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.Freelancer;

public interface FreelancerRepository extends JpaRepository<Freelancer,Integer> {
    
    Optional<Freelancer> findByEmail(String email);
    boolean existsByEmail(String email);
}
