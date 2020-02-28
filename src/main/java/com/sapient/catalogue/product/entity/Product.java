package com.sapient.catalogue.product.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SKU_ID")
    private Long id;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "SUPPLIER")
    private String supplier;
    @Column(name = "COLOUR")
    private String colour;
    @Column(name = "SIZE")
    private String size;

}
