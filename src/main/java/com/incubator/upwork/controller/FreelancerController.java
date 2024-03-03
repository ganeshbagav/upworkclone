package com.incubator.upwork.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incubator.upwork.services.FreelancerService;

@RestController
@RequestMapping("/upwork/freelancer")
public class FreelancerController {

    @Autowired
    private FreelancerService freelancerService;

    @PostMapping("/sign-up") //
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> entity) {
        System.out.println("freelancer signup");
        if (entity.get("email").equals(null)) {
            Map<String, String> response = new HashMap<String, String>();
            response.put("message", "email missing");
            return ResponseEntity.ok().body(response);
        }
        return freelancerService.signUp(entity);
    }

    @PostMapping("/dashboard") 
    public ResponseEntity<?> dashboard(@RequestBody Map<String, String> entity) {
        System.out.println("Freelancer Dashboard");

        return freelancerService.dashboard(entity);
    }

    @PostMapping("/add-balance") 
    public ResponseEntity<?> addBalance(Map<String, String> entity) {
        System.out.println("add balance");
        return freelancerService.addBalance(entity);
    }

    @PostMapping("/send-job-proposal") //
    public ResponseEntity<?> jobProposal(@RequestBody Map<String, String> entity) {
        return freelancerService.jobProposal(entity);
    }

    @PostMapping("/search-job") //
    public ResponseEntity<?> searchJob(@RequestBody Map<String, List<String>> entity) {
        return freelancerService.searchJob(entity);
    }


    @PostMapping("/submit-milestone")
    public <T> ResponseEntity<?> submitMilestone(@RequestBody Map<String, T> entity) {
        return freelancerService.submitMilestone(entity);
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
    public <T> ResponseEntity<?> getProjectsInfo(@RequestBody Map<String, String> entity) {
        System.out.println("get project info");
        return freelancerService.getProjectsInfo(entity);
    }

    @PostMapping("/get-freelancer-feed")
    public <T> ResponseEntity<?> getFreelancerFeed(@RequestBody Map<String, String> entity) {
        return freelancerService.getFreelancerFeed(entity);
    }

    @PostMapping("/my-applied-jobs")
    public <T> ResponseEntity<?> getAppliedJobPost(@RequestBody Map<String, String> entity) {
        return freelancerService.getAppliedJobPost(entity);
    }

}



// /* (API not required) from the given projectId it gives result of is the
// project complete or not */
// @PostMapping("/get-isprojectcomplete")
// public <T> ResponseEntity<?> getCompletionProject(@RequestBody Map<String, T>
// entity) {
// return freelancerService.getCompletionProject(entity);
// }
// /* (API not required) from the given clientId return all project under that
// client */
// @PostMapping("/get-all-projects")
// public <T> ResponseEntity<?> getAllProjects(@RequestBody Map<String, T>
// entity) {
// return freelancerService.getAllProjects(entity);
// }