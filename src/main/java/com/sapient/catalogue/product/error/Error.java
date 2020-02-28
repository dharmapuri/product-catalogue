package com.sapient.catalogue.product.error;

import lombok.Data;

@Data
public class Error {
  private  int code;
  private  String message = null;
  private  String referenceId = null;
}