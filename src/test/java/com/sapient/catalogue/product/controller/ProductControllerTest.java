package com.sapient.catalogue.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sapient.catalogue.product.model.ProductModel;
import com.sapient.catalogue.product.response.PagingResponse;
import com.sapient.catalogue.product.response.ProductsResponse;
import com.sapient.catalogue.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

  @MockBean
  private ProductService productService;

  @Autowired
  private MockMvc mockMvc;

  ProductsResponse productsResponse;

  @BeforeEach
  void setup() {
    productsResponse = new ProductsResponse();
    List<ProductModel> productModelList = getProductModels();
    productsResponse.setProducts(productModelList);
    PagingResponse pagingResponse = new PagingResponse();
    pagingResponse.setTotalNumberOfRecords(2);
    pagingResponse.setPageSize(5);
    pagingResponse.setPageNumber(0);
    pagingResponse.setTotalNoOfPages(0);
    productsResponse.setPagingResponse(pagingResponse);
  }

  @Test
  void productsApiShouldReturnSuccessCodeWithProductDetails() throws Exception {
    when(productService.fetchAllProductsByFacets(any())).thenReturn(productsResponse);
    mockMvc
        .perform(
            get("/products/")
                    .param("page", "0")
                    .param("pageSize", "1")
                    .param("sortBy", "colour"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.products[0].category").value("shirts"))
        .andExpect(jsonPath("$.products[0].supplier").value("ABC"))
        .andExpect(jsonPath("$.products[0].colour").value("Red"))
        .andExpect(jsonPath("$.products[0].size").value("XL"));
  }

  @Test
  void invalidEndPointShouldReturnNotFoundCode() throws Exception {
    when(productService.fetchAllProductsByFacets(any())).thenReturn(productsResponse);
    mockMvc
        .perform(
            get("/products/invalid")
                .param("page", "0")
                .param("pageSize", "1")
                .param("sortBy", "colour"))
        .andExpect(status().isNotFound());
  }

  @Test
  void productsApiShouldReturnPagingMetaDataInResponse() throws Exception {
    when(productService.fetchAllProductsByFacets(any())).thenReturn(productsResponse);
    mockMvc
        .perform(
            get("/products/").param("page", "0").param("pageSize", "1").param("sortBy", "colour"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andDo(print())
        .andExpect(jsonPath("$.pagingResponse").exists())
        .andExpect(jsonPath("$.pagingResponse.totalNumberOfRecords").value("2"))
        .andExpect(jsonPath("$.pagingResponse.pageSize").value("5"))
        .andExpect(jsonPath("$.pagingResponse.totalNoOfPages").value("0"));
  }

  @Test
  void productsApiShouldReturnResultWithSortedByColour() throws Exception {
    when(productService.fetchAllProductsByFacets(any())).thenReturn(productsResponse);
    mockMvc
        .perform(
            get("/products/")
                    .param("page", "0")
                    .param("pageSize", "1")
                    .param("sortBy", "colour"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.products[0].colour").hasJsonPath());
  }

  @Test
  void productsApiBySkuIdShouldReturnProduct() throws Exception {
    when(productService.findBySkuId(any())).thenReturn(getProductModels().get(0));
    mockMvc
        .perform(
            get("/products/1/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.category").value("shirts"))
        .andExpect(jsonPath("$.supplier").value("ABC"))
        .andExpect(jsonPath("$.colour").value("Red"))
        .andExpect(jsonPath("$.size").value("XL"));
  }



  @Test
  void productsApiWithInvalidRequestShouldReturnBadRequest() throws Exception {
    when(productService.addProduct(any())).thenReturn(123L);
    mockMvc.perform(post("/products/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getProductModels().get(0).toString()))
            .andExpect(status().isBadRequest());

  }

  @Test
  void productsApiWithProductDetailsShouldReturnCreatedHTTPCode() throws Exception {
    when(productService.addProduct(any())).thenReturn(123L);
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    mockMvc.perform(post("/products/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(getProductModels().get(0))))
            .andExpect(status().isCreated());
  }

  private List<ProductModel> getProductModels() {
    ProductModel productModel = new ProductModel();
    productModel.setCategory("shirts");
    productModel.setColour("Red");
    productModel.setSize("XL");
    productModel.setSupplier("ABC");
    ProductModel productModelBlack = new ProductModel();
    productModelBlack.setCategory("shirts");
    productModelBlack.setColour("Black");
    productModelBlack.setSize("XL");
    productModelBlack.setSupplier("ABC");
    List<ProductModel> productModelList = new ArrayList<>();
    productModelList.add(productModel);
    productModelList.add(productModelBlack);
    return productModelList;
  }
}
