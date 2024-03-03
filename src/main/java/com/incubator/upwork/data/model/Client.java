package com.incubator.upwork.data.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String country;
    private String companyName;
    private String website;
    private String industry;
    private String empStrength; // (How many people are working in your company?)
    private String description;
    private String tagLine;
    private String owner;
    private String compPhone;
    private String address;
    private String city;
    private String pincode;
    private float balance;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JobPost> job = new ArrayList<>();

    public void addJobPost(JobPost jobPost) {
        job.add(jobPost);
        jobPost.setClient(this);
    }

    public void removeJobPost(JobPost jobPost) {
        job.remove(jobPost);
        jobPost.setClient(null);
    }

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setClient(this);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
        notification.setClient(null);

    }
}
