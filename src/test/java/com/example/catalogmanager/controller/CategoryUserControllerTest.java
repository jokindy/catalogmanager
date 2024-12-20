package com.example.catalogmanager.controller;

import static com.example.catalogmanager.util.CategoryTestDataFactory.createCategory;
import static com.example.catalogmanager.util.CategoryTestDataFactory.createCategoryPage;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalogmanager.domain.Category;
import com.example.catalogmanager.repository.CategoryRepository;
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
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryUserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CategoryRepository categoryRepository;

  @Test
  void testGetCategoryById_Success() throws Exception {
    Category category = createCategory();

    Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    mockMvc
        .perform(get("/api/v1/categories/1"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.name").value("Category 1"),
            jsonPath("$.description").value("Description for category 1"),
            jsonPath("$.logoUrl").value("https://category1_logo.jpeg"));
  }

  @Test
  void testGetCategoryByNonExistentId_404() throws Exception {
    mockMvc
        .perform(get("/api/v1/categories/1"))
        .andExpectAll(
            status().isNotFound(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(1)),
            jsonPath("$.faults[0].message", is("Entity not found")),
            jsonPath("$.faults[0].reason", is("Wrong id")));
  }

  @Test
  void testGetCategoryByInvalidId_400() throws Exception {
    mockMvc
        .perform(get("/api/v1/categories/AA"))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(1)),
            jsonPath("$.faults[0].message", is("Invalid parameter: id")));
  }

  @Test
  void testGetAllCategories_Success() throws Exception {
    Mockito.when(categoryRepository.findAll(any(Pageable.class))).thenReturn(createCategoryPage());

    mockMvc
        .perform(get("/api/v1/categories"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$", hasSize(3)),
            jsonPath("$[0].id").value(1),
            jsonPath("$[0].name").value("Category 1"),
            jsonPath("$[0].description").value("Description for category 1"),
            jsonPath("$[0].logoUrl").value("category1_logo.jpeg"),
            jsonPath("$[1].id").value(2),
            jsonPath("$[1].name").value("Category 2"),
            jsonPath("$[1].description").value("Description for category 2"),
            jsonPath("$[1].logoUrl").value("category2_logo.jpeg"),
            jsonPath("$[2].id").value(3),
            jsonPath("$[2].name").value("Category 3"),
            jsonPath("$[2].description").value("Description for category 3"),
            jsonPath("$[2].logoUrl").value("category3_logo.jpeg"));
  }

  @Test
  void testGetAllCategories_EmptyList() throws Exception {
    Mockito.when(categoryRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(new ArrayList<>()));

    mockMvc
        .perform(get("/api/v1/categories"))
        .andExpectAll(status().isOk(), jsonPath("$", hasSize(0)));
  }

  @Test
  void testGetAllCategories_InvalidRequestParams() throws Exception {
    mockMvc
        .perform(get("/api/v1/categories").param("pageSize", "-2").param("pageNumber", "-9"))
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
