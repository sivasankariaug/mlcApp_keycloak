package com.mlc.repository;

import com.mlc.entity.ApplicationMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationMenuRepository extends JpaRepository<ApplicationMenu, Long> {
}
