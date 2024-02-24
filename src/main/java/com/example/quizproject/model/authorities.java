package com.example.quizproject.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class authorities {
    @Id
    @SequenceGenerator(name="a_id",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int a_id;
    @NonNull

    private String username;
    @NonNull
    private String authority;
    @OneToOne
    @JoinColumn(name="user_id",referencedColumnName = "U_id")
    private User user;

    public authorities(String username,String authority){
        this.username=username;
        this.authority=authority;
    }

}
