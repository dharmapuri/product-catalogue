package com.sapient.catalogue.product.controller;

import com.sapient.catalogue.product.error.Error;
import com.sapient.catalogue.product.model.ProductModel;
import com.sapient.catalogue.product.request.ProductRequestParams;
import com.sapient.catalogue.product.response.ProductsResponse;
import com.sapient.catalogue.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/")
@RequiredArgsConstructor
@Api(value = "Product Catalogue ")
@Slf4j
public class ProductController {

  private final ProductService productService;

  @ExceptionHandler({MissingServletRequestParameterException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public com.sapient.catalogue.product.error.Error handleMissingParams(MissingServletRequestParameterException ex) {
    Error error = new Error();
    error.setCode(400);
    error.setMessage("Missing mandatory parameter " + ex.getParameterName());
    error.setReferenceId("PRODUCT_INVALID_INPUT");
    return error;
  }

  @ApiOperation(
      value = "Get the list of products",
      notes = "This API retrieves all the matching product details based on provided facet values",
      response = ProductModel.class)
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 200,
            message = "Success",
            response = ProductModel.class,
            responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 405, message = "Method not allowed", response = Error.class),
        @ApiResponse(code = 404, message = "Not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
      })
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductsResponse getAllProducts(
          @ModelAttribute("productRequestParams") ProductRequestParams productRequestParams) {
    return productService.fetchAllProductsByFacets(productRequestParams);
  }

  @ApiOperation(
      value = "Get the product details by skuId",
      notes = "API retrieves details of individual product based on given SKU ID",
      response = ProductModel.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 200, message = "Success", response = ProductModel.class),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 405, message = "Method not allowed", response = Error.class),
        @ApiResponse(code = 404, message = "Not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
      })
  @GetMapping(value = "{skuId}/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductModel getProductBySkuId(@PathVariable(value = "skuId") Long skuId) {
    log.info("Product Id :{}",skuId);
    return productService.findBySkuId(skuId);
  }

  @ApiOperation(
      value = "Add new product details to existing catalogue",
      notes = "This API enables supplier to add new product for sale",
      response = ProductModel.class)
  @ApiResponses(
      value = {
        @ApiResponse(code = 201, message = "Created", response = ProductModel.class),
        @ApiResponse(code = 400, message = "Bad request", response = Error.class),
        @ApiResponse(code = 405, message = "Method not allowed", response = Error.class),
        @ApiResponse(code = 404, message = "Not found", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
      })
  // Just added @valid here it is not having good significant but in realistic worth to have it in
  // controller layer
  // We can replace this DTO by having separate Request POJO like ProductRequest.class
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus( HttpStatus.CREATED )
  public Long addProduct(@RequestBody ProductModel productModel) {
    return productService.addProduct(productModel);
  }

  @ModelAttribute("ProductRequestParams")
  private ProductRequestParams setRequestParameters(
          @RequestParam(value = "page", defaultValue = "0") String page,
          @RequestParam(value = "pageSize", defaultValue = "5") String pageSize,
          @RequestParam(value = "category", required = false) String category,
          @RequestParam(value = "colour", required = false) String colour,
          @RequestParam(value = "brand", required = false) String brand,
          @RequestParam(value = "size", required = false) String size,
          @RequestParam(value = "sortBy", required = false) String sortBy ) {

    ProductRequestParams productRequestParams = new ProductRequestParams();
    productRequestParams.setBrand(brand);
    productRequestParams.setCategory(category);
    productRequestParams.setColour(colour);
    productRequestParams.setSize(size);
    productRequestParams.setPage(page);
    productRequestParams.setPageSize(pageSize);
    productRequestParams.setSortBy(sortBy);
    return productRequestParams;
  }
}
