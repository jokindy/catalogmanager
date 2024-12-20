package com.example.catalogmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.catalogmanager.domain.Category;
import com.example.catalogmanager.repository.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.yml")
class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CategoryRepository categoryRepository;

  @Test
  @WithMockUser(username = "testuser")
  void testGetBook() throws Exception {
    Category category = new Category();
    category.setName("Test category");
    category.setDescription("Test description");
    category.setLogoUrl("logourl.jpeg");

    Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    mockMvc
        .perform(get("/api/v1/categories/1"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.name").value("Test category"),
            jsonPath("$.description").value("Test description"),
            jsonPath("$.logoUrl").value("logourl.jpeg"));
  }
}
