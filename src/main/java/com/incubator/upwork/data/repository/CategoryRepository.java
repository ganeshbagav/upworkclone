package com.incubator.upwork.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
