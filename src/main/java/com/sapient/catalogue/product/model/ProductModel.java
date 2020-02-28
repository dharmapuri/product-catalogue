package com.sapient.catalogue.product.model;

import lombok.Data;

@Data
public class ProductModel {

    private Long id;
    private String colour;
    private String category;
    private Double price;
    private String supplier;
    private String size;
}
