package com.example.quizproject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="quiz_students")
public class quiz_students {
    @Id
    @SequenceGenerator(name="quiz_stud_id",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "U_id")
    private User user;
    @ManyToOne
    @JoinColumn(referencedColumnName = "quiz_id")
    private Quiz quiz;

}
