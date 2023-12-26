package com.mlc.repository;

import com.mlc.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster,Long> {
    @Query(value = "SELECT * FROM user_master  WHERE user_name = :userName",nativeQuery = true)
    UserMaster getUserMasterByUserName(@Param("userName") String userName);
}
