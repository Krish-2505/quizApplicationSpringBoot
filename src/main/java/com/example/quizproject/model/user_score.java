package com.example.quizproject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class user_score {
    @Id
    @SequenceGenerator(name = "usq_id_gen",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int usq_id;
    @ManyToOne
    @JoinColumn(referencedColumnName = "username")
    private User user;
    @ManyToOne
    @JoinColumn(referencedColumnName = "subject_name")
    private subject sub;
    public user_score(User user, subject sub,int i) {
        this.user=user;
        this.sub=sub;
        this.total_score=i;
    }

    public int getTotal_score() {
        return total_score;
    }

    private int total_score=0;

}
