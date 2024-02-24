package com.example.quizproject.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class question_wrapper {
    private int q_id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int score;
}
