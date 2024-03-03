package com.incubator.upwork.data.model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "jobpost")
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;
    private String jobTitle;
    @Column(length = 10000)
    private String jobDescription;
    private String category;
    private Integer started;
    // private String skills;
    private String scope;// (Long term, short-term)
    private String budget;
    private Time time;
    private Date date;
    private int hide;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "client")
    private Client client;

    @OneToMany(mappedBy = "jobpost", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<JobProposals> jobProposals = new ArrayList<>();

    public void addJobProposals(JobProposals jobProposal) {
        jobProposals.add(jobProposal);
        jobProposal.setJobpost(this);
    }

    public void removeJobProposals(JobProposals jobProposal) {
        jobProposals.remove(jobProposal);
        jobProposal.setJobpost(null);
    }

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<SkillTags> skillTags = new ArrayList<>();

    public void addSkillTags(SkillTags skillTag) {
        skillTags.add(skillTag);
        skillTag.setJobPost(this);
    }

    public void removeSkillTags(SkillTags skillTag) {
        skillTags.remove(skillTag);
        skillTag.setJobPost(null);
    }

}
