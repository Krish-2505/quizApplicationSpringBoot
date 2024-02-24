package com.example.quizproject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.security.auth.Subject;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Quiz")
public class Quiz {
    @Id
    @SequenceGenerator(name="quiz_id",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int quiz_id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "username")
    private User user;

    public Quiz(User user, subject sub_id, String start_time, String endtime, int score) {
        this.user = user;
        this.subid = sub_id;
        this.starttime = start_time;
        this.endtime = endtime;
        this.score = score;
    }

    @ManyToOne
    @JoinColumn(referencedColumnName = "subject_name")
    private subject subid;
    private String starttime;
    private String endtime;
    private int score;

    public Quiz(User user, subject sub_id, String start_time) {
        this.user = user;
        this.subid = sub_id;
        this.starttime = start_time;
    }
}
