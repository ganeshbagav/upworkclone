package com.incubator.upwork.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incubator.upwork.data.repository.AdminRepository;
import com.incubator.upwork.services.AdminService;

@RestController
@RequestMapping("/upwork/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;

    // ApI-1 = Add Skills one string at a time
    @PostMapping("/add-skills")
    public ResponseEntity<?> addSkills(@RequestBody Map<String, List<String>> entity) {

        return adminService.addSkills(entity);
    }

    // API-2 = Get List of skills by words while searching
    @PostMapping("/get-skills-by-word")
    public ResponseEntity<?> getSkills(@RequestBody Map<String, String> entity) {
        return adminService.getSkills(entity);
    }

    // API-3 = Get List of all skills
    @GetMapping("/get-all-skills")
    public ResponseEntity<?> getAllSkills() {
        return adminService.getAllSkills();
    }

    // ApI-4 = Add Category one string at a time
    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> entity) {

        return adminService.addCategory(entity);
    }

    // API-5 = Get List of all categories
    @GetMapping("/get-all-categories")
    public ResponseEntity<?> getAllCategories() {
        return adminService.getAllCategories();
    }

    // ApI-6 = Add Industries one string at a time
    @PostMapping("/add-industry")
    public ResponseEntity<?> addIndustry(@RequestBody Map<String, String> entity) {

        return adminService.addIndustry(entity);
    }

    // API-7 = Get List of all industries
    @GetMapping("/get-all-industries")
    public ResponseEntity<?> getAllIndustries() {
        return adminService.getAllIndustries();
    }

}
