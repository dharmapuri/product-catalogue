package com.sapient.catalogue.product.service;

import com.sapient.catalogue.product.entity.Product;
import com.sapient.catalogue.product.mapper.SourceToDestinationMapper;
import com.sapient.catalogue.product.model.ProductModel;
import com.sapient.catalogue.product.repository.ProductRepository;
import com.sapient.catalogue.product.request.ProductRequestParams;
import com.sapient.catalogue.product.response.PagingResponse;
import com.sapient.catalogue.product.response.ProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;
  private final SourceToDestinationMapper sourceToDestinationMapper;

  public ProductsResponse fetchAllProductsByFacets(ProductRequestParams productRequestParams) {
    Pageable pageRequest = PageRequest.of(Integer.parseInt(productRequestParams.getPage()),
            Integer.parseInt(productRequestParams.getPageSize()));
    Product product = new Product();
    product.setCategory(productRequestParams.getCategory());
    product.setSize(productRequestParams.getSize());
    product.setColour(productRequestParams.getColour());
    product.setSupplier(productRequestParams.getBrand());
    Example<Product> productExample = Example.of(product);
    return getProductsResponse(pageRequest, productExample);
  }

  public ProductsResponse getProductsResponse(Pageable pageRequest, Example<Product> productExample) {
    Page<Product> pagedProducts = productRepository.findAll(productExample, pageRequest);
    ProductsResponse productsResponse = new ProductsResponse();
    PagingResponse pagingResponse = new PagingResponse();
    productsResponse.setProducts(sourceToDestinationMapper.entityToDtoList( pagedProducts.getContent()));
    pagingResponse.setHashNextPage(pagedProducts.nextPageable().isPaged());
    pagingResponse.setHashPreviousPage(pagedProducts.previousPageable().isPaged());
    pagingResponse.setPageNumber(pagedProducts.getNumber());
    pagingResponse.setPageSize(pagedProducts.getSize());
    pagingResponse.setTotalNoOfPages(pagedProducts.getTotalPages());
    pagingResponse.setTotalNumberOfRecords(pagedProducts.getTotalElements());
    log.info("Total no of records: {}", pagedProducts.getTotalElements());
    productsResponse.setPagingResponse(pagingResponse);
    return productsResponse;
  }

  public ProductModel findBySkuId(Long skuId) {
    Optional<Product> product = productRepository.findById(skuId);
    return sourceToDestinationMapper.entityToDto(product.orElse(null));
  }

  public Long addProduct(ProductModel productModel) {
    Product product = sourceToDestinationMapper.dtoToEntity(productModel);
    log.info("Product added to Catalogue with Id: {}", product.getId());
    return productRepository.save(product).getId();
  }

}
