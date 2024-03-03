package com.incubator.upwork.controller;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incubator.upwork.data.model.Project;
import com.incubator.upwork.services.ClientService;

@RestController
@RequestMapping("/upwork/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> entity) {
        System.out.println("client signup");
        if (entity.get("email").equals(null)) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("message", "email missing");
            return ResponseEntity.ok().body(response);
        }
        return clientService.signUp(entity);
    }

    @PostMapping("/dashboard")
    public ResponseEntity<?> dashboard(@RequestBody Map<String, String> entity) {
        System.out.println("Client Dashboard");

        return clientService.dashboard(entity);
    }

    @PostMapping("/get-my-job-post")
    public <T> ResponseEntity<?> myJobPost(@RequestBody Map<String, String> entity) {
        System.out.println("My Job Post");

        return clientService.myJobPost(entity);
    }

    @PostMapping("/add-job-post")
    public <T> ResponseEntity<?> addJobPost(@RequestBody Map<String, T> entity) {
        System.out.println("Add Job Post");

        return clientService.addJobPost(entity);
    }

    /*
     * It is used to get all details about the projects under that user
     * 
     * parameters :
     * 
     * email : ganesh@gmail.com
     * projectStatus : complete or incomplete
     */
    @PostMapping("/get-projects-info")
    public ResponseEntity<?> getProjectsInfo(@RequestBody Map<String, String> entity) {
        System.out.println("get project info");
        return clientService.getProjectsInfo(entity);
    }

    @PostMapping("/add-balance")
    public <T> ResponseEntity<?> addBalance(@RequestBody Map<String, String> entity) {
        System.out.println("add balance");
        return clientService.addBalance(entity);
    }

    @PostMapping("/search-talent")
    public <T> ResponseEntity<?> searchTalent(@RequestBody Map<String, String> entity) {
        return null;
    }

    @PostMapping("/get-job-post-proposals")
    public <T> ResponseEntity<?> getJobPostProposal(@RequestBody Map<String, String> entity) {
        System.out.println("get-job-post-proposals");
        return clientService.getJobPostProposal(entity);
    }

    @PostMapping("/start-project")
    public <T> ResponseEntity<?> startProject(@RequestBody Map<String, T> entity) {
        System.out.println("start project");
        return clientService.startProject(entity);
    }

    @PostMapping("/verify-milestone")
    public <T> ResponseEntity<?> completeMilestone(@RequestBody Map<String, T> entity) {
        System.out.println("verify milestone");
        return clientService.completeMilestone(entity);
    }

    /*
     * When a project's all milestones are achieved, it make change in the database
     * of project completion
     */
    @PostMapping("/complete-project")
    public <T> ResponseEntity<?> completeProject(@RequestBody Map<String, T> entity) {
        return clientService.completeProject(entity);
    }

    @PostMapping("/get-project-milestones")
    public <T> ResponseEntity<?> getProjectMilestone(@RequestBody Map<String, T> entity) {
        return clientService.getProjectMilestone(entity);
    }
}
