package com.incubator.upwork.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.incubator.upwork.data.model.Transction;

public interface TransctionRepository extends JpaRepository<Transction, Integer> {

    @Query(value = "SELECT * FROM transction t WHERE t.fromphone = :phone OR j.tophone = :phone ORDER BY date DESC,time DESC", nativeQuery = true)
    public List<Transction> findAllByPhoneOrderByTimeAndDate(String phone);
}
