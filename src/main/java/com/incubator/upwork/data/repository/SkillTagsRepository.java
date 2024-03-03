package com.incubator.upwork.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.incubator.upwork.data.model.SkillTags;

import jakarta.transaction.Transactional;

public interface SkillTagsRepository extends JpaRepository<SkillTags,Integer>{

    public List<SkillTags> findBySkillsIgnoreCaseContaining(String title);
    public Optional<SkillTags> findBySkills(String skill);
    @Transactional
    public void deleteBySkills(String skill);

    // @Transactional
    // void deleteAllByCourseid(int cid);
}
