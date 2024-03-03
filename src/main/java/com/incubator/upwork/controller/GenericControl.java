package com.incubator.upwork.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incubator.upwork.services.ClientService;
import com.incubator.upwork.services.FreelancerService;
import com.incubator.upwork.services.GenericService;

@RestController
@RequestMapping("/upwork")
public class GenericControl {

    @Autowired
    private FreelancerService freelancerService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private GenericService genericService;

    // API-1=All Users Login
    @PostMapping("/log-in")
    public ResponseEntity<?> logIn(@RequestBody Map<String, String> entity) {

        System.out.println("login");
        Map<String, String> response = new HashMap<String, String>();
        if (entity.get("email").equals(null)) {
            response.put("message", "email missing");
            return ResponseEntity.ok().body(response);
        }
        if (freelancerService.existsByEmail(entity.get("email"))) {
            return freelancerService.logIn(entity);
        } else if (clientService.existsByEmail(entity.get("email"))) {
            return clientService.logIn(entity);
        } else {
            response.put("message", "sign up first");
            return ResponseEntity.ok().body(response);
        }

    }

    @PostMapping("/demo")
    public ResponseEntity<?> demo() {
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", "done");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/notification")
    public ResponseEntity<?> Notification(@RequestBody Map<String, String> entity) {

        return genericService.Notification(entity);
    }

    @PostMapping("/transaction")
    public <T> ResponseEntity<?> transaction(@RequestBody Map<String, T> entity) {

        return genericService.transaction(entity);
    }

    @PostMapping("/viewed-notification")
    public ResponseEntity<?> viewedNotification(@RequestBody Map<String, String> entity) {

        return genericService.viewedNotification(entity);
    }

}
