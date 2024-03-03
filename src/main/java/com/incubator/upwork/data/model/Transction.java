package com.incubator.upwork.data.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

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
@Table(name = "transction")
public class Transction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transctionId;
    private Time time;
    private Date date;
    private String from;
    private String fromphone;
    private String to;
    private String tophone;
    private float amount;
    private int projectid;

    // @OneToOne
    // @JoinColumn(name = "milestoneid")
    // private MileStone milestone;

    @OneToMany(mappedBy = "transction", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MileStone> mileStones = new ArrayList<>();

    public void addMileStone(MileStone mileStone) {
        mileStones.add(mileStone);
        mileStone.setTransction(this);
    }

    public void removeMileStone(MileStone mileStone) {
        mileStones.remove(mileStone);
        mileStone.setTransction(null);
    }

}
