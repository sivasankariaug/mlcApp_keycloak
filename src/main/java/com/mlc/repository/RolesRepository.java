package com.mlc.repository;

import com.mlc.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
    @Query(value = "SELECT * FROM roles  WHERE name = :roleName",nativeQuery = true)
    Roles getRolesByRoleName(@Param("roleName") String roleName);
}
