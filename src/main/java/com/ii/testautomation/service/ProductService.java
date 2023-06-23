package com.ii.testautomation.service;

import java.util.List;
import com.ii.testautomation.dto.request.ProductRequest;
import com.ii.testautomation.dto.response.ProductResponse;


public interface ProductService {

  public List<ProductResponse> getAllProducts();

  public void createProduct(ProductRequest productRequest);
  
  public ProductResponse getProductById(Long id);
  
  public void deleteProductById(Long id);

}
