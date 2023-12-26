package com.mlc.repository;

import java.util.List;

import com.mlc.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(value = "SELECT p FROM Product p WHERE " +
            "p.product_name LIKE CONCAT('%',:query, '%')" +
            "Or p.material LIKE CONCAT('%', :query, '%')",nativeQuery = true)
	List<Product> searchProducts(String query);

}
