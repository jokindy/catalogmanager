package com.example.catalogmanager.controller;

import com.example.catalogmanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
// @TestPropertySource(locations = "classpath:application-test.yml")
public class ProductAbstractControllerTest {
  @Autowired protected MockMvc mockMvc;

  @MockBean protected CategoryRepository categoryRepository;
}
