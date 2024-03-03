package com.incubator.upwork.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.Message;

public interface MessageRepository extends JpaRepository<Message,Integer>{
    
}
