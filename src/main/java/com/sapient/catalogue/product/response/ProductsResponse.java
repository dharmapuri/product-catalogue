package com.sapient.catalogue.product.response;

import com.sapient.catalogue.product.model.ProductModel;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Data
public class ProductsResponse extends ResourceSupport {

    private List<ProductModel> products;
    private PagingResponse pagingResponse;
}
