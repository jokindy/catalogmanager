package com.example.catalogmanager.error;

import java.time.LocalDateTime;

public class ErrorDto {
  private int status;
  private String message;
  private String operation;
  private LocalDateTime timestamp;

  public ErrorDto(int status, String message, String operation) {
    this.status = status;
    this.message = message;
    this.operation = operation;
    this.timestamp = LocalDateTime.now();
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
