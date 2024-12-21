package com.example.catalogmanager.controller;

import static com.example.catalogmanager.util.ProductFactory.createProduct;
import static com.example.catalogmanager.util.ProductFactory.createProductsPage;
import static com.example.catalogmanager.util.ProductFactory.createSameCategoryProductsPage;
import static com.example.catalogmanager.util.ProductFactory.createSameNameProductsPage;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalogmanager.domain.Product;
import com.example.catalogmanager.repository.ProductRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "/application.yml")
class ProductUserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ProductRepository productRepository;

  @Test
  void testGetProductById_Success() throws Exception {
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
  void testGetProductByNonExistentId_404() throws Exception {
    mockMvc
        .perform(get("/api/v1/products/1"))
        .andExpectAll(
            status().isNotFound(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(1)),
            jsonPath("$.faults[0].message", is("Entity not found")),
            jsonPath("$.faults[0].reason", is("Wrong id")));
  }

  @Test
  void testGetProductByInvalidId_400() throws Exception {
    mockMvc
        .perform(get("/api/v1/products/AA"))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(1)),
            jsonPath("$.faults[0].message", is("Invalid parameter: id")));
  }

  @Test
  void testGetAllProducts_Success() throws Exception {
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
  void testSearchProductsByName_Success() throws Exception {
    Mockito.when(productRepository.findByName(any(), any(Pageable.class)))
        .thenReturn(createSameNameProductsPage());

    mockMvc
        .perform(get("/api/v1/products/search").param("name", "Test product name 1"))
        .andExpectAll(status().isOk(), jsonPath("$", hasSize(2)))
        .andExpectAll(
            jsonPath("$[0].name", is("Test product name 1")),
            jsonPath("$[0].description", is("Test product description 1")),
            jsonPath("$[0].price", is(9.99)),
            jsonPath("$[0].stockQuantity", is(30)),
            jsonPath("$[0].category.name", is("Category 2")),
            jsonPath("$[0].category.description", is("Description for category 2")))
        .andExpectAll(
            jsonPath("$[1].name", is("Test product name 1")),
            jsonPath("$[1].description", is("Test product description 2")),
            jsonPath("$[1].price", is(19.99)),
            jsonPath("$[1].stockQuantity", is(40)),
            jsonPath("$[1].category.name", is("Category 3")),
            jsonPath("$[1].category.description", is("Description for category 3")));
  }

  @Test
  void testSearchProductsByCategoryName_Success() throws Exception {
    Mockito.when(productRepository.findProductsByCategory_Name(any(), any(Pageable.class)))
        .thenReturn(createSameCategoryProductsPage());

    mockMvc
        .perform(get("/api/v1/products/category").param("categoryName", "Category 1"))
        .andExpectAll(status().isOk(), jsonPath("$", hasSize(2)))
        .andExpectAll(
            jsonPath("$[0].name", is("Test product name 1")),
            jsonPath("$[0].description", is("Test product description 1")),
            jsonPath("$[0].price", is(9.99)),
            jsonPath("$[0].stockQuantity", is(30)),
            jsonPath("$[0].category.name", is("Category 1")),
            jsonPath("$[0].category.description", is("Description for category 1")))
        .andExpectAll(
            jsonPath("$[1].name", is("Test product name 2")),
            jsonPath("$[1].description", is("Test product description 2")),
            jsonPath("$[1].price", is(19.99)),
            jsonPath("$[1].stockQuantity", is(40)),
            jsonPath("$[1].category.name", is("Category 1")),
            jsonPath("$[1].category.description", is("Description for category 1")));
  }

  @Test
  void testGetAllProducts_EmptyList() throws Exception {
    Mockito.when(productRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(new ArrayList<>()));

    mockMvc
        .perform(get("/api/v1/products"))
        .andExpectAll(status().isOk(), jsonPath("$", hasSize(0)));
  }

  @Test
  void testGetAllProducts_InvalidRequestParams() throws Exception {
    mockMvc
        .perform(get("/api/v1/products").param("pageSize", "-2").param("pageNumber", "-9"))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(2)),
            jsonPath(
                "$.faults[*].message",
                containsInAnyOrder("Invalid parameter: pageSize", "Invalid parameter: pageNumber")),
            jsonPath(
                "$.faults[*].reason",
                containsInAnyOrder(
                    "Must be greater than or equal to 0", "Must be greater than 0")));
  }
}
