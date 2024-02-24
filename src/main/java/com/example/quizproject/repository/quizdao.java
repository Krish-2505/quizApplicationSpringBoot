package com.example.quizproject.repository;


import com.example.quizproject.model.Quiz;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface quizdao extends JpaRepository<Quiz,Integer> {

    List<Quiz> findAllByUser_username(String username,PageRequest of);
    List<Quiz> findAllByUser_username(String username);
    @Query(value="SELECT * FROM quiz p WHERE p.user_username=%?2% and ( p.subid_subject_name LIKE %?1% OR CONCAT(p.score, '') LIKE %?1%)",nativeQuery = true)
    public List<Quiz> search(String keyword,String username,PageRequest of);
    @Query(value="SELECT * FROM quiz p WHERE p.user_username=%?2% and ( p.subid_subject_name LIKE %?1% OR CONCAT(p.score, '') LIKE %?1%)",nativeQuery = true)
    public List<Quiz> search(String keyword,String username);
    @Transactional
    @Modifying
    @Query(value="update quiz set score=?2, endtime=?3 where quiz_id=?1",nativeQuery = true)
    void updatetimeandscore(int id,int score,String endtime);
    @Transactional
    @Modifying
    @Query(value="delete from quiz where user_username=?1",nativeQuery = true)
    void deleteAllByUsername(String s);
    @Query(value="SELECT * FROM quiz p WHERE p.user_username LIKE %?1% OR p.subid_subject_name LIKE %?1% OR CONCAT(p.score, '') LIKE %?1%",nativeQuery = true)
    public List<Quiz> findAll(String keyword,PageRequest of);
    @Query(value="SELECT * FROM quiz p WHERE p.user_username LIKE %?1% OR p.subid_subject_name LIKE %?1% OR CONCAT(p.score, '') LIKE %?1%",nativeQuery = true)
    public List<Quiz> findAll(String keyword);
    @Modifying
    @Transactional
    @Query(value ="delete from quiz where user_username=?1 and endtime is NULL",nativeQuery = true)
    void delete_empendtime(String username);

}
