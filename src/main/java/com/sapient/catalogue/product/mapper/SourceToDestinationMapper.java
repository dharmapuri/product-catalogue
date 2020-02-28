package com.sapient.catalogue.product.mapper;

import com.sapient.catalogue.product.model.ProductModel;
import com.sapient.catalogue.product.entity.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SourceToDestinationMapper {

   Product dtoToEntity(ProductModel productModel);

   ProductModel entityToDto(Product product);
   @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
   List<Product> dtoToEntityList(List<ProductModel> productModel);
   @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
   List<ProductModel> entityToDtoList(List<Product> product);
}
