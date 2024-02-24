package com.example.quizproject.model;

import com.example.quizproject.service.authservice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users",uniqueConstraints =@UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @SequenceGenerator(name="u_id",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int U_id;
    @NonNull

    private String username;
    private String firstname;
    private  String lastname;
    @NonNull
    private String password;
    private int enabled=1;


    public User(@NonNull String username, String firstname, String lastname, @NonNull String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }



    @Override
    public String toString() {
        return "User{" +
                "U_id=" + U_id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +

                '}';
    }

}
