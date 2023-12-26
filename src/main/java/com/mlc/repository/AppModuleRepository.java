package com.mlc.repository;

import com.mlc.entity.AppModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppModuleRepository extends JpaRepository<AppModule,Long> {

}
