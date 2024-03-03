package com.incubator.upwork.data.model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;
    private Date startDate;
    private float budget;
    private Float clientdeposit;
    private Float freelancerdeposit;
    private Date endDate;
    private Integer clientid;
    private Integer jobpostid;
    private Integer freelancerid;
    private Integer iscomplete;
    private Integer onGoingMileStone;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MileStone> mileStones = new ArrayList<>();

    public void addMileStone(MileStone mileStone) {
        mileStones.add(mileStone);
        mileStone.setProject(this);
    }

    public void removeMileStone(MileStone mileStone) {
        mileStones.remove(mileStone);
        mileStone.setProject(null);
    }

    

}
