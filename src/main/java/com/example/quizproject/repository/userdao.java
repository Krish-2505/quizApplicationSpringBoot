package com.example.quizproject.repository;


import com.example.quizproject.model.Quiz;
import com.example.quizproject.model.User;
import com.example.quizproject.model.authorities;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Repository
public interface userdao extends JpaRepository<User,Integer> {
//    @Query("SELECT u FROM users u WHERE u.username = :username")
    public User findByUsername(String username);
    @Modifying
    @Transactional
    public void deleteById(int id);
    @Query(value="SELECT * FROM users p WHERE p.username LIKE %?1% OR p.firstname LIKE %?1%  OR p.lastname LIKE %?1% ",nativeQuery = true)
    public List<User> findAll(String keyword);
    @Query(value="SELECT * FROM users p WHERE p.username LIKE %?1% OR p.firstname LIKE %?1%  OR p.lastname LIKE %?1% ",nativeQuery = true)
    public List<User> findAll(String keyword, PageRequest of);
}
