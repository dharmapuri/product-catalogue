package com.sapient.catalogue.product.service;

import com.sapient.catalogue.product.entity.Product;
import com.sapient.catalogue.product.mapper.SourceToDestinationMapper;
import com.sapient.catalogue.product.model.ProductModel;
import com.sapient.catalogue.product.repository.ProductRepository;
import com.sapient.catalogue.product.request.ProductRequestParams;
import com.sapient.catalogue.product.response.ProductsResponse;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest
public class ProductServiceTest {

    private ProductsResponse productsResponse;

    @Autowired
    private ProductService productService;

    @Autowired
    private SourceToDestinationMapper sourceToDestinationMapper;

    @MockBean
    private ProductRepository productRepository;



    @Test
    void responseShouldHavePageNumberAndPageSizeDetails() {
        ProductRequestParams productRequestParams = new ProductRequestParams();
        productRequestParams.setSortBy("colour");
        productRequestParams.setPage("0");
        productRequestParams.setPageSize("3");
        ProductsResponse productsResponse = productService.fetchAllProductsByFacets(productRequestParams);
        assertThat(productsResponse.getPagingResponse().getPageNumber(),  is(0L));
        assertThat(productsResponse.getPagingResponse().getPageSize(),  is(3L));
    }


    @Test
    void shouldReturnPagedProductResponse() {
        ProductRequestParams productRequestParams = new ProductRequestParams();
        productRequestParams.setSortBy("colour");
        productRequestParams.setPage("0");
        productRequestParams.setPageSize("3");
        ProductsResponse productsResponse = productService.fetchAllProductsByFacets(productRequestParams);
        assertNull(productsResponse);
    }

    @Test
    @Ignore
    void shouldAddNewProductToCatalogue() {
        Long productId = productService.addProduct(getProductModels().get(0));
        assertNull(productId);
    }

    @Test
    @Ignore
    void shouldReturnProductDetailsBySkuId(Long skuId) {
       when(productRepository.findById(100L)).thenReturn(java.util.Optional.of(new Product()));
        ProductModel productModel = productService.findBySkuId(100L);
        assertNull(productModel);
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
