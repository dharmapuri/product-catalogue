package com.sapient.catalogue.product.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel
public class ProductRequestParams {
  @ApiParam(value = "This parameter used to provide page number", allowableValues = "1")
  private String page;

  @ApiParam(value = "This parameter used to provide no of records per page", allowableValues = "5")
  private String pageSize;

  @ApiParam(
      value = "This is optional parameter used to filter products by category",
      allowableValues = "shirts,pants,socks,caps")
  private String category;

  @ApiParam(
      value = "This is optional parameter used to filter products by price -range",
      allowableValues = "100.0")
  private String price;

  @ApiParam(
      value = "This is optional parameter used to filter products by price -range",
      allowableValues = "green,yellow,red,blue,white,black etc")
  private String colour;

  @ApiParam(
      value = "This is optional parameter used to filter products by brand name",
      example = "Reebok,Raymond,Peter England etc")
  private String brand;

  @ApiParam(
          value = "This is optional parameter used to filter products by size",
          example = "XS,S,M,XL,XXL,XXL")
  private String size;

  @ApiParam(
          value = "This is optional parameter used to sort by facet",
          example = "size,colour,brand,category")
  private String sortBy;


}
