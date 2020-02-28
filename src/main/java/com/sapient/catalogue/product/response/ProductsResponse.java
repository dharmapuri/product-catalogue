package com.sapient.catalogue.product.response;

import com.sapient.catalogue.product.model.ProductModel;
import lombok.Data;

import java.util.List;

@Data
public class ProductsResponse {

    private List<ProductModel> products;
    private PagingResponse pagingResponse;
}
