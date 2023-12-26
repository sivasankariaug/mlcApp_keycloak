package com.mlc.controller;

import com.mlc.entity.Product;
import com.mlc.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProductControllerTest {
@Autowired
    private MockMvc mockMvc;
@Autowired
private ProductRepository productRepository;
@Autowired
private ObjectMapper objectMapper;
@BeforeEach
void setup()
{
    productRepository.deleteAll();
}
@Test
public void givenProductObject_whenCreatedProduct_thenReturnSavedProduct() throws Exception
{
    Product product= Product.builder()
            .id(1)
            .productName("SoapBottle")
            .material("MaterialA")
            .weight(12)
            .numberOfComponents(100)
            .build();

    ResultActions response = mockMvc.perform(post("/v1/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product)));


    response.andDo(print()).
            andExpect(status().isCreated())
            .andExpect(jsonPath("$.product_Name",is(product.getProductName())))
            .andExpect(jsonPath("$.material",is(product.getMaterial())))
            .andExpect(jsonPath("$.weight",is(product.getWeight())))
            .andExpect(jsonPath("$.number_of_components",is(product.getNumberOfComponents())));

}

    @Test
    public void givenListOfProducts_whenGetAllProducts_thenReturnproductList() throws Exception{

        List<Product> listOfProducts = new ArrayList<>();
        listOfProducts.add(Product.builder().id(1).productName("soapBottle").material("materialA").weight(10.00).numberOfComponents(100).build());
        listOfProducts.add(Product.builder().id(2).productName("shampooBottle").material("materialB").weight(11.00).numberOfComponents(200).build());
        productRepository.saveAll(listOfProducts);
        ResultActions response = mockMvc.perform(get("/v1/view"));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfProducts.size())));

    }

    @Test
    public void givenProductId_whenGetProductById_thenReturnEmployeeObject() throws Exception{



        Product product = Product.builder()
                .productName("SOAPBOTTLE")
                .material("MaterialA")
                .weight(10.00)
                .numberOfComponents(100)
                .build();
        productRepository.save(product);


        ResultActions response = mockMvc.perform(get("/v1/viewById").param("id", String.valueOf(product.getId())));



        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.product_Name", is(product.getProductName())))
                .andExpect(jsonPath("$.material", is(product.getMaterial())))
                .andExpect(jsonPath("$.weight", is(product.getWeight())))
                .andExpect(jsonPath("$.number_of_components", is(product.getProductName())));

    }


    @Test
    public void givenInvalidProductId_whenGetProductById_thenReturnEmpty() throws Exception{

        long id = 1L;
        Product product = Product.builder()
                .productName("SoapBottle")
                .material("MaterialA")
                .weight(10.00)
                .numberOfComponents(100)
                .build();
        productRepository.save(product);


        ResultActions response = mockMvc.perform(get("/v1/viewById").param("id", String.valueOf(id)));


        response.andExpect(status().isNotFound())
                .andDo(print());

    }
















}
