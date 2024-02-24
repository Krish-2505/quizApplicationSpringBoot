package com.example.quizproject.repository;


import com.example.quizproject.model.subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface subjectdao extends JpaRepository<subject,Integer> {
    @Query(value="Select * from subject s where s.subject_name=?1",nativeQuery = true)
    subject findBySubject_name(String subject_name);
    @Transactional
    @Modifying
    @Query(value="Update subject set total_score=?1 where subject_name=?2",nativeQuery = true)
    void updatescore(int score,String subject_name);
}
