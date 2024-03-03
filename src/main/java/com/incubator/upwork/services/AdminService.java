package com.incubator.upwork.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incubator.upwork.data.model.Category;
import com.incubator.upwork.data.model.Industries;
import com.incubator.upwork.data.model.SkillTags;
import com.incubator.upwork.data.repository.CategoryRepository;
import com.incubator.upwork.data.repository.IndustriesRepository;
import com.incubator.upwork.data.repository.SkillTagsRepository;

@Service
public class AdminService {
    @Autowired
    private SkillTagsRepository skillTagsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IndustriesRepository industriesRepository;

    static Map<String, String> response = new WeakHashMap<>();

    public ResponseEntity<?> addSkills(Map<String, List<String>> entity) {
        response.clear();
        List<String> skills = entity.get("skills");
        for (String part : skills) {
            SkillTags skillTags = SkillTags.builder().skills(part).build();
            skillTagsRepository.save(skillTags);
        }

        response.put("message", "Successful");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<?> getSkills(Map<String, String> entity) {
        response.clear();
        String skill = entity.get("skills");
        List<SkillTags> title = skillTagsRepository.findBySkillsIgnoreCaseContaining(skill);
        Set<String> titles = new TreeSet();
        for (SkillTags part : title) {
            titles.add(part.getSkills());
        }
        return ResponseEntity.ok().body(titles);
    }

    public ResponseEntity<?> getAllSkills() {
        List<SkillTags> title = skillTagsRepository.findAll();
        Set<String> titles = new TreeSet();
        for (SkillTags part : title) {
            titles.add(part.getSkills());
        }
        return ResponseEntity.ok().body(titles);
    }

    public ResponseEntity<?> addCategory(Map<String, String> entity) {
        response.clear();
        String categorystr = entity.get("category");
        Category category = Category.builder().categories(categorystr).build();
        categoryRepository.save(category);
        response.put("message", "Category Added Sucessfully!!!...");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok().body(categoryRepository.findAll());
    }

    public ResponseEntity<?> addIndustry(Map<String, String> entity) {
        response.clear();
        String Industrystr = entity.get("industry");
        Industries industries = Industries.builder().industryName(Industrystr).build();
        industriesRepository.save(industries);
        response.put("message", "Industry Added Sucessfully!!!...");
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<?> getAllIndustries() {
        return ResponseEntity.ok().body(industriesRepository.findAll());
    }
}
