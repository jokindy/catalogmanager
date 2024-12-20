package com.example.catalogmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalogmanager.domain.Category;
import com.example.catalogmanager.dto.category.CategoryCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

class ProductAdminControllerTest extends ProductAbstractControllerTest {
  @Autowired private ObjectMapper objectMapper;

  @Test
  @WithMockUser(username = "admin", roles = {"admin"})
  void testCreateNewCategory() throws Exception {
    CategoryCreateDto category = new CategoryCreateDto();
    category.setName("Test category");
    category.setDescription("Test description");
    category.setLogoUrl("http://google.com");

    //   Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    mockMvc
        .perform(post("/api/v1/categories").content(objectMapper.writeValueAsString(category)))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.name").value("Test category"),
            jsonPath("$.description").value("Test description"),
            jsonPath("$.logoUrl").value("logourl.jpeg"));
  }
}
