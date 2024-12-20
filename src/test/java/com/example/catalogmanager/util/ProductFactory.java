package com.example.catalogmanager.util;

import com.example.catalogmanager.domain.Category;
import com.example.catalogmanager.domain.Product;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class ProductFactory {
  public static Page<Product> createProductsPage() {
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

  public static Page<Product> createSameNameProductsPage() {
    Product product1 = new Product();
    product1.setId(1L);
    product1.setName("Test product name 1");
    product1.setDescription("Test product description 1");
    product1.setPrice(BigDecimal.valueOf(9.99));
    product1.setStockQuantity(30);

    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Test product name 1");
    product2.setDescription("Test product description 2");
    product2.setPrice(BigDecimal.valueOf(19.99));
    product2.setStockQuantity(40);

    Category category2 = new Category();
    category2.setName("Category 2");
    category2.setDescription("Description for category 2");

    Category category3 = new Category();
    category3.setName("Category 3");
    category3.setDescription("Description for category 3");

    product1.setCategory(category2);
    product2.setCategory(category3);

    return new PageImpl<>(List.of(product1, product2));
  }

  public static Page<Product> createSameCategoryProductsPage() {
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

    Category category2 = new Category();
    category2.setName("Category 1");
    category2.setDescription("Description for category 1");

    product1.setCategory(category2);
    product2.setCategory(category2);

    return new PageImpl<>(List.of(product1, product2));
  }

  public static Product createProduct() {
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
