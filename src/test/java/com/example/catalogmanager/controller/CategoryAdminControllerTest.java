package com.example.catalogmanager.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalogmanager.domain.Category;
import com.example.catalogmanager.dto.category.CategoryCreateDto;
import com.example.catalogmanager.dto.category.CategoryUpdateDto;
import com.example.catalogmanager.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class CategoryAdminControllerTest {

  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;
  @Autowired private CategoryRepository categoryRepository;

  @Test
  void testCreateCategory_Success() throws Exception {
    CategoryCreateDto categoryDto = new CategoryCreateDto();
    categoryDto.setName("Test category");
    categoryDto.setDescription("Test description");
    categoryDto.setLogoUrl("http://google.com");

    mockMvc
        .perform(
            post("/api/v1/categories")
                .content(objectMapper.writeValueAsString(categoryDto))
                .contentType(ContentType.APPLICATION_JSON.toString()))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.name").value("Test category"),
            jsonPath("$.description").value("Test description"),
            jsonPath("$.logoUrl").value("http://google.com"));
  }

  @Test
  void testCreateCategory_InvalidDto() throws Exception {
    CategoryCreateDto categoryDto = new CategoryCreateDto();
    categoryDto.setName("1");
    categoryDto.setLogoUrl("sgooglescom");

    mockMvc
        .perform(
            post("/api/v1/categories")
                .content(objectMapper.writeValueAsString(categoryDto))
                .contentType(ContentType.APPLICATION_JSON.toString()))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(3)),
            jsonPath(
                "$.faults[*].message",
                containsInAnyOrder(
                    "Invalid parameter: name",
                    "Invalid parameter: description",
                    "Invalid parameter: logoUrl")),
            jsonPath(
                "$.faults[*].reason",
                containsInAnyOrder(
                    "Must not me null",
                    "Must be between 3 and 85 characters",
                    "Must be a valid url")));
  }

  @Test
  void testUpdateCategory_InvalidDto() throws Exception {
    CategoryUpdateDto categoryDto = new CategoryUpdateDto();
    categoryDto.setName("1");
    categoryDto.setLogoUrl("sgooglescom");

    mockMvc
        .perform(
            put("/api/v1/categories")
                .content(objectMapper.writeValueAsString(categoryDto))
                .contentType(ContentType.APPLICATION_JSON.toString()))
        .andExpectAll(
            status().isBadRequest(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(4)),
            jsonPath(
                "$.faults[*].message",
                containsInAnyOrder(
                    "Invalid parameter: id",
                    "Invalid parameter: name",
                    "Invalid parameter: description",
                    "Invalid parameter: logoUrl")),
            jsonPath(
                "$.faults[*].reason",
                containsInAnyOrder(
                    "Must not me null",
                    "Must not me null",
                    "Must be between 3 and 85 characters",
                    "Must be a valid url")));
  }

  @Test
  void testUpdateCategory_NonExistingId() throws Exception {
    CategoryUpdateDto categoryDto = new CategoryUpdateDto();
    categoryDto.setId(44L);
    categoryDto.setName("New name");
    categoryDto.setDescription("New description");
    categoryDto.setLogoUrl("http://new-logo.com");

    mockMvc
        .perform(
            put("/api/v1/categories")
                .content(objectMapper.writeValueAsString(categoryDto))
                .contentType(ContentType.APPLICATION_JSON.toString()))
        .andExpectAll(
            status().isNotFound(),
            jsonPath("$.timestamp", notNullValue()),
            jsonPath("$.traceId", notNullValue()),
            jsonPath("$.faults", hasSize(1)),
            jsonPath("$.faults[0].message", is("Entity not found")),
            jsonPath("$.faults[0].reason", is("Wrong id")));
  }

  @Test
  void testUpdateCategory_Success() throws Exception {
    Long categoryId = prepareCategoryForUpdate();

    CategoryUpdateDto categoryDto = new CategoryUpdateDto();
    categoryDto.setId(categoryId);
    categoryDto.setName("New name");
    categoryDto.setDescription("New description");
    categoryDto.setLogoUrl("http://new-logo.com");

    mockMvc
        .perform(
            put("/api/v1/categories")
                .content(objectMapper.writeValueAsString(categoryDto))
                .contentType(ContentType.APPLICATION_JSON.toString()))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.name").value("New name"),
            jsonPath("$.description").value("New description"),
            jsonPath("$.logoUrl").value("http://new-logo.com"));
  }

  private Long prepareCategoryForUpdate() {
    Category category = new Category();
    category.setName("Test category");
    category.setDescription("Test description");
    category.setLogoUrl("http://google.com");

    return categoryRepository.save(category).getId();
  }
}
