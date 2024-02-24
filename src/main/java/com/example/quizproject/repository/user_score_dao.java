package com.example.quizproject.repository;

import com.example.quizproject.model.user_score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface user_score_dao extends JpaRepository<user_score,Integer> {
//    @Query(value = "select user")
    List<user_score> findAllByUser_username(String username);
    @Transactional
    @Modifying
    @Query(value = "Update user_score set total_score=?3 where user_username=?1 and sub_subject_name=?2",nativeQuery = true)
    public void updatescore(String username,String subject_name,int score);

    @Query(value = "select * from user_score u where u.user_username=?1 and u.sub_subject_name=?2",nativeQuery = true)
    user_score findByUser_usernameandsub(String username,String subject_name);
     @Transactional
    @Modifying
    @Query(value = "delete from user_score where user_username=?1",nativeQuery = true)
    void deleteAllByUsername(String s);
}
