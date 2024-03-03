package com.incubator.upwork.data.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "freelancer")
public class Freelancer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer freelancerId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String country;
    private String categories; // ( Software developer, Full stack , photographer )
    // private String skills; // (Android, React, and Software Development)
    private String languages;
    private String experience; // (Entry level, intermediate, expert )

    private String phone;
    private String projectPreference; // (Long term, short-term, both)
    private String about;
    private String workHistory;
    private String portfolio;
    private String employmentHistory;
    private String certifications;

    private int connect;
    private float balance;
    private float earnings;

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JobProposals> jobProposals = new ArrayList<>();

    public void addJobProposals(JobProposals jobProposal) {
        jobProposals.add(jobProposal);
        jobProposal.setFreelancer(this);
    }

    public void removeJobProposals(JobProposals jobProposal) {
        jobProposals.remove(jobProposal);
        jobProposal.setFreelancer(null);
    }

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setFreelancer(this);
    }

    public void removeNotification(Notification notification) {
        notifications.remove(notification);
        notification.setFreelancer(null);

    }

    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SkillTags> skillTags = new ArrayList<>();

    public void addSkillTags(SkillTags skillTag) {
        skillTags.add(skillTag);
        skillTag.setFreelancer(this);
    }

    public void removeSkillTags(SkillTags skillTag) {
        skillTags.remove(skillTag);
        skillTag.setFreelancer(null);
    }
}
