package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.DTO.UserDto;
import com.example.demo.enums.Specialization;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
//import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    
    @Id
    private String doctorId;
    
    @OneToOne
    @MapsId
    @JsonBackReference
    @JoinColumn(name = "doctor_id")
    private User userDoctor;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "specialization", nullable = false)
    @Enumerated(EnumType.STRING)
    private Specialization specialization;
    
    
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + doctorId + '\'' +
                ", name='" + name + '\'' +
                ", specialization=" + specialization +
                '}';
    }
}

