package com.example.catalogmanager.error.exception;

public class GeneralException extends RuntimeException {
  private int status;
  private String message;

  public GeneralException(int status, String message) {
    super();
    this.status = status;
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
