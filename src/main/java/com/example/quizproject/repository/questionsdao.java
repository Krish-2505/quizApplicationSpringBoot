package com.example.quizproject.repository;

import com.example.quizproject.model.Quiz;
import com.example.quizproject.model.questions;
import com.example.quizproject.model.subject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface questionsdao extends JpaRepository<questions,Integer> {
    @Query(value ="Select * from questions q where q.subject_name_subject_name=?1",nativeQuery = true)
    List<questions> findAllBySubject_name(String subject_name);
    @Query(value="SELECT * FROM questions p WHERE p.question LIKE %?1% OR p.subject_name_subject_name LIKE %?1% OR CONCAT(p.score, '') LIKE %?1%",nativeQuery = true)
    List<questions> findAll(String keyword);
    @Query(value="SELECT * FROM questions p WHERE p.question LIKE %?1% OR p.subject_name_subject_name LIKE %?1% OR CONCAT(p.score, '') LIKE %?1%",nativeQuery = true)
    List<questions> findAll(String keyword, PageRequest of);
}
