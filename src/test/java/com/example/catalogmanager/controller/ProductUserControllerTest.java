package com.example.catalogmanager.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalogmanager.domain.Category;
import com.example.catalogmanager.domain.Product;
import com.example.catalogmanager.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "/application-test.yml")
public class ProductUserControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private ProductRepository productRepository;

  @Test
  void testGetCategoryById_Success() throws Exception {
    Product product = createProduct();

    Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    mockMvc
        .perform(get("/api/v1/products/1"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.name", is("Test product name")),
            jsonPath("$.description", is("Test product description")),
            jsonPath("$.price", is(9.99)),
            jsonPath("$.stockQuantity", is(30)),
            jsonPath("$.category.name", is("Test category name")),
            jsonPath("$.category.description", is("Test category description")));
  }

  @Test
  void testGetCategoryByNonExistentId_404() throws Exception {
    mockMvc
        .perform(get("/api/v1/products/1"))
        .andExpectAll(
            status().isNotFound(),
            jsonPath("$.message", is("Entity not found")),
            jsonPath("$.operation", is("GET /api/v1/products/1")));
  }

  @Test
  void testGetCategoryByInvalidId_400() throws Exception {
    mockMvc
        .perform(get("/api/v1/products/AA"))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$", hasSize(1)),
            jsonPath("$[0].message").value("Invalid parameter: id"),
            jsonPath("$[0].operation").value("GET /api/v1/products/AA"));
  }

  @Test
  void testGetAllCategories_Success() throws Exception {
    Mockito.when(productRepository.findAll(any(Pageable.class))).thenReturn(createProductsPage());

    mockMvc
        .perform(get("/api/v1/products"))
        .andExpectAll(status().isOk(), jsonPath("$", hasSize(3)))
        .andExpectAll(
            jsonPath("$[0].name", is("Test product name 1")),
            jsonPath("$[0].description", is("Test product description 1")),
            jsonPath("$[0].price", is(9.99)),
            jsonPath("$[0].stockQuantity", is(30)),
            jsonPath("$[0].category.name", is("Category 2")),
            jsonPath("$[0].category.description", is("Description for category 2")))
        .andExpectAll(
            jsonPath("$[1].name", is("Test product name 2")),
            jsonPath("$[1].description", is("Test product description 2")),
            jsonPath("$[1].price", is(19.99)),
            jsonPath("$[1].stockQuantity", is(40)),
            jsonPath("$[1].category.name", is("Category 3")),
            jsonPath("$[1].category.description", is("Description for category 3")))
        .andExpectAll(
            jsonPath("$[2].name", is("Test product name 3")),
            jsonPath("$[2].description", is("Test product description 3")),
            jsonPath("$[2].price", is(29.99)),
            jsonPath("$[2].stockQuantity", is(50)),
            jsonPath("$[2].category.name", is("Category 2")),
            jsonPath("$[2].category.description", is("Description for category 2")));
  }

  @Test
  void testGetAllCategories_EmptyList() throws Exception {
    Mockito.when(productRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(new ArrayList<>()));

    mockMvc
        .perform(get("/api/v1/products"))
        .andExpectAll(status().isOk(), jsonPath("$", hasSize(0)));
  }

  @Test
  void testGetAllCategories_InvalidRequestParams() throws Exception {
    mockMvc
        .perform(get("/api/v1/products").param("pageSize", "-2").param("pageNumber", "-9"))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$", hasSize(2)),
            jsonPath(
                "$[*].message",
                containsInAnyOrder("Invalid parameter: pageSize", "Invalid parameter: pageNumber")),
            jsonPath(
                "$[*].operation",
                containsInAnyOrder("GET /api/v1/products", "GET /api/v1/products")));
  }

  private Page<Product> createProductsPage() {
    Product product1 = new Product();
    product1.setId(1L);
    product1.setName("Test product name 1");
    product1.setDescription("Test product description 1");
    product1.setPrice(BigDecimal.valueOf(9.99));
    product1.setStockQuantity(30);

    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Test product name 2");
    product2.setDescription("Test product description 2");
    product2.setPrice(BigDecimal.valueOf(19.99));
    product2.setStockQuantity(40);

    Product product3 = new Product();
    product3.setId(3L);
    product3.setName("Test product name 3");
    product3.setDescription("Test product description 3");
    product3.setPrice(BigDecimal.valueOf(29.99));
    product3.setStockQuantity(50);

    Category category2 = new Category();
    category2.setName("Category 2");
    category2.setDescription("Description for category 2");

    Category category3 = new Category();
    category3.setName("Category 3");
    category3.setDescription("Description for category 3");

    product1.setCategory(category2);
    product2.setCategory(category3);
    product3.setCategory(category2);

    return new PageImpl<>(List.of(product1, product2, product3));
  }

  private Product createProduct() {
    Product product = new Product();
    product.setName("Test product name");
    product.setDescription("Test product description");
    product.setPrice(BigDecimal.valueOf(9.99));
    product.setStockQuantity(30);

    Category category = new Category();

    category.setName("Test category name");
    category.setDescription("Test category description");

    product.setCategory(category);

    return product;
  }
}
