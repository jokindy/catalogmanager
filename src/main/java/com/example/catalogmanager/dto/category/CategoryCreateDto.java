package com.example.catalogmanager.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class CategoryCreateDto {
  @NotNull
  @Size(min = 3, max = 85)
  private String name;

  @NotNull
  @Size(min = 3, max = 255)
  private String description;

  @NotNull
  @Size(min = 3, max = 255)
  @URL
  private String logoUrl;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }
}
