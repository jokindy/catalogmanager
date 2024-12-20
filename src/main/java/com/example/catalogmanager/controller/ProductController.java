package com.example.catalogmanager.controller;

import com.example.catalogmanager.dto.product.ProductCreateDto;
import com.example.catalogmanager.dto.product.ProductUpdateDto;
import com.example.catalogmanager.dto.product.ProductViewDto;
import com.example.catalogmanager.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<List<ProductViewDto>> getAllProducts(
      @PositiveOrZero @RequestParam(defaultValue = "0") int pageNumber,
      @Positive @RequestParam(defaultValue = "10") int pageSize) {
    List<ProductViewDto> products = productService.getAllProducts(pageNumber, pageSize);
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductViewDto> getProductById(@PathVariable Long id) {
    ProductViewDto product = productService.getProductById(id);
    return ResponseEntity.ok(product);
  }

  @GetMapping("/search")
  public ResponseEntity<List<ProductViewDto>> searchProducts(
      @RequestParam String name,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<ProductViewDto> products = productService.searchProductsByName(name, page, size);
    return ResponseEntity.ok(products);
  }

  @PostMapping
  // @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ProductViewDto> createProduct(
      @Valid @RequestBody ProductCreateDto productCreateDto) {
    ProductViewDto createdProduct = productService.createProduct(productCreateDto);
    return ResponseEntity.ok(createdProduct);
  }

  @PutMapping
  // @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<ProductViewDto> updateProduct(
      @Valid @RequestBody ProductUpdateDto productUpdateDto) {
    ProductViewDto updatedProduct = productService.updateProduct(productUpdateDto);
    return ResponseEntity.ok(updatedProduct);
  }

  @DeleteMapping("/{id}")
  // @PreAuthorize("hasRole('EDITOR')")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/category")
  public ResponseEntity<List<ProductViewDto>> getProductsByCategoryName(
      @RequestParam String categoryName,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    List<ProductViewDto> products =
        productService.getProductsByCategoryName(categoryName, page, size);
    return ResponseEntity.ok(products);
  }
}
