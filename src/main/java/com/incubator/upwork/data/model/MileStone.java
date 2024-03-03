package com.incubator.upwork.data.model;

import java.sql.Date;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "milestone")
public class MileStone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer milestoneId;
    private String Title;
    private String Description;
    private float payment;
    private Date startDate;
    private Date endDate;
    private Integer submission;
    private Integer milestonenum;
    private Integer verify; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "projectid")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "transction")
    private Transction transction;

}
