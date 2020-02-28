package com.sapient.catalogue.product.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@EnableSwagger2
@Configuration
@EnableWebMvc
public class SwaggerConfiguration {

  private final TypeResolver typeResolver;

  @Autowired
  public SwaggerConfiguration(final TypeResolver typeResolver) {
    this.typeResolver = typeResolver;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.sapient.catalogue.product.controller"))
        .paths(PathSelectors.regex("/.*"))
        .build()
        .useDefaultResponseMessages(false)
        .additionalModels(typeResolver.resolve(Error.class))
        .globalResponseMessage(RequestMethod.GET, defaultMessages())
        .globalResponseMessage(RequestMethod.POST, defaultMessages())
        .globalResponseMessage(RequestMethod.PATCH, defaultMessages())
        .globalResponseMessage(RequestMethod.PUT, defaultMessages())
        .globalResponseMessage(RequestMethod.DELETE, defaultMessages())
        .forCodeGeneration(true);
  }

  private List<ResponseMessage> defaultMessages() {
    ModelRef errorRef = new ModelRef("Error");

    return Lists.newArrayList(
        new ResponseMessageBuilder()
            .code(400)
            .message("Bad Request")
            .responseModel(errorRef)
            .build(),
        new ResponseMessageBuilder()
            .code(401)
            .message("Unauthorized")
            .responseModel(errorRef)
            .build(),
        new ResponseMessageBuilder().code(404).message("Not Found").responseModel(errorRef).build(),
        new ResponseMessageBuilder()
            .code(500)
            .message("Internal Server Error")
            .responseModel(errorRef)
            .build(),
        new ResponseMessageBuilder()
            .code(503)
            .message("Service Unavailable")
            .responseModel(errorRef)
            .build());
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");
      }
    };
  }
}
