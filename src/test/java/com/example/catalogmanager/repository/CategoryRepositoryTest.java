package com.example.catalogmanager.repository;

import com.example.catalogmanager.domain.Category;
import jakarta.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CategoryRepositoryTest {

  @Autowired private DataSource dataSource;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private EntityManager entityManager;
  @Autowired private CategoryRepository categoryRepository;

  @Test
  void injectedComponentsAreNotNull() {
    Assertions.assertNotNull(dataSource);
    Assertions.assertNotNull(jdbcTemplate);
    Assertions.assertNotNull(entityManager);
    Assertions.assertNotNull(categoryRepository);
  }

  @Test
  void saveCategory() {
    Category category = new Category();
    category.setName("Test category");
    category.setDescription("Test description");
    category.setLogoUrl("logourl.jpeg");

    categoryRepository.save(category);

    Assertions.assertNotNull(categoryRepository.findById(1L).get());
  }
}
