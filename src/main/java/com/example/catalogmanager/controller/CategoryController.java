package com.example.catalogmanager.controller;

import com.example.catalogmanager.dto.category.CategoryCreateDto;
import com.example.catalogmanager.dto.category.CategoryUpdateDto;
import com.example.catalogmanager.dto.category.CategoryViewDto;
import com.example.catalogmanager.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseEntity<List<CategoryViewDto>> getAllCategories(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(categoryService.getAllCategories(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryViewDto> getCategoryById(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.getCategoryById(id));
  }

  @PostMapping
  public ResponseEntity<CategoryViewDto> addCategory(
      @Valid @RequestBody CategoryCreateDto categoryCreateDto) {
    return ResponseEntity.ok(categoryService.addCategory(categoryCreateDto));
  }

  @PutMapping
  public ResponseEntity<CategoryViewDto> updateCategory(
      @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
    return ResponseEntity.ok(categoryService.updateCategory(categoryUpdateDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
