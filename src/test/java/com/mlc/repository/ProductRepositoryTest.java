package com.mlc.repository;

import com.mlc.entity.Product;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

@Test
@Order(1)
@Rollback(value = false)
    public void saveProductTest()
    {
    Product product = Product.builder()
            .productName("soapbottle")
            .material("materialc")
            .weight(29)
            .numberOfComponents(23)
            .build();
    productRepository.save(product);
        assertThat(product.getId()).isGreaterThan(0);
    }
    @Test
    @Order(2)
    public void getProductTest(){

        Product product = productRepository.findById(1L).get();

        assertThat(product.getId()).isEqualTo(1L);

    }

@Test
@Order(3)
public void getListOfProductsTest()
{
    List<Product> product = productRepository.findAll();
    assertThat(product.size()).isGreaterThan(0);
}
    @Test
    @Order(4)
    @Rollback(value = false)
    public void deleteProductTest(){

        Product product = productRepository.findById(1L).get();

        productRepository.delete(product);

        productRepository.deleteById(1L);

        Product product1 = null;

        Optional<Product> optionalProduct = productRepository.findById(1L);
    if(optionalProduct.isPresent())
    {
        product1=optionalProduct.get();

    }

        assertThat(product1).isNull();


    }
@Test
@Order(5)

    public void searchProducttest()
{
    Product product = Product.builder()
            .productName("soapbottle")
            .material("materialc")
            .weight(9)
            .numberOfComponents(21)
            .build();
    Product product1 = Product.builder()
            .productName("shampoobottle")
            .material("materialA")
            .weight(10)
            .numberOfComponents(32)
            .build();

    Product product2 = Product.builder()
            .productName("petbottle")
            .material("materialb")
            .weight(19)
            .numberOfComponents(35)
            .build();

productRepository.saveAll(List.of(product,product1,product2));
List<Product> result = productRepository.searchProducts("soap");
assertThat(result).hasSize(1);

}

}
