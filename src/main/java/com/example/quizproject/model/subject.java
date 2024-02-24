package com.example.quizproject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="subject" ,uniqueConstraints = @UniqueConstraint(columnNames = "subject_name"))
public class subject {
    @Id
    @SequenceGenerator(name="quiz_id",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sub_id;


   private String subject_name;

    public subject(String subject_name,int total_score) {
        this.subject_name=subject_name;
        this.total_score=total_score;
    }
    private int total_score;
}
