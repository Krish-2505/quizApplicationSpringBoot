package com.example.quizproject.repository;

import com.example.quizproject.model.authorities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface authdao extends JpaRepository<authorities,Integer> {
//    @Modifying
//    @Transactional
//    @Query(value="Delete FROM authorities u WHERE u.user_id = ?id")
//    void deleteByUser_Id(int id);

    public authorities findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value="Delete FROM authorities u WHERE u.username = :username",nativeQuery = true)
    void deleteByUsername(@Param("username") String username);
//    @Query("DELETE u FROM authorities u WHERE u.user_id = :id")
//    public authorities findByUser_id(int u_id);
    @Query(value="select count(*) from authorities a where a.authority='ROLE_admin' and a.username!=?1",nativeQuery = true)
    public int getadmincount(String username);
    @Query(value="select * from authorities a where a.user_id=?1",nativeQuery = true)
    authorities findByUser_id(int id);
}
