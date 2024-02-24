package com.example.quizproject.model;


import jakarta.persistence.*;

import java.util.List;

//@Entity
public class user_selected_options {
    @Id
    private int uso_id;

    @ManyToMany
    private List<questions> questions;
    @OneToMany
    private User user;
    @OneToOne
    private Quiz quiz;

}
