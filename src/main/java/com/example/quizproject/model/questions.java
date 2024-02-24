package com.example.quizproject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="questions")
public class questions {
    @Id
    @SequenceGenerator(name="q_id",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int q_id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String Correctoption;
    private int score=2;
    @ManyToOne
    @JoinColumn(referencedColumnName = "subject_name")
    private subject subject_name;

    public questions(String question, String option1, String option2, String option3, String option4, String correctoption, int score, subject subject_name) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        Correctoption = correctoption;
        this.score = score;
        this.subject_name = subject_name;
    }
}
