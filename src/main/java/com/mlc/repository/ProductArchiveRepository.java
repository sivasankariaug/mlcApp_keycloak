package com.mlc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mlc.entity.ArchiveProduct;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductArchiveRepository extends JpaRepository<ArchiveProduct, Long> {

}