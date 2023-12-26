package com.mlc.controller;

import java.util.List;
import java.util.Optional;
import com.mlc.entity.Product;
import com.mlc.entity.Response;
import com.mlc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/product")
public class ProductController {

	private final ProductService productService;


	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;

	}

	@GetMapping("/v1/view")
	public ResponseEntity<List<Product>> viewAllProducts() {
		List<Product> products = productService.viewAllProducts();

		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@GetMapping("/v1/viewById/{id}")
	public ResponseEntity<Product> viewById(@PathVariable long id) {
		Optional<Product> product = productService.viewProductById(id);
		return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("v1/create")
	public ResponseEntity<Response> saveProduct(@RequestBody Product product) {
		Response response = productService.saveProduct(product);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("v1/delete/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("v1/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam("query") String query) {
		return ResponseEntity.ok(productService.searchProducts(query));
	}

}