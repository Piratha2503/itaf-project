package com.ii.testautomation.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ii.testautomation.dto.request.ProductRequest;
import com.ii.testautomation.dto.response.ProductResponse;
import com.ii.testautomation.entities.Product;
import com.ii.testautomation.repositories.ProductRepository;
import com.ii.testautomation.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
  @Autowired
  private ProductRepository productRepository;
  
  @Transactional
  public List<ProductResponse> getAllProducts() {
    List<ProductResponse> productResponseList=new ArrayList<ProductResponse>();
    for(Product product:productRepository.findAll()) {
      ProductResponse productResponse=new ProductResponse();
          BeanUtils.copyProperties(product, productResponse);
          productResponseList.add(productResponse);
    }
    return productResponseList;
  }

  @Override
  public void createProduct(ProductRequest productRequest) {
    Product product=new Product();
    BeanUtils.copyProperties(productRequest, product);
    productRepository.save(product);
  }

  @Override
  public ProductResponse getProductById(Long id) {
    ProductResponse productResponse=new ProductResponse();
    BeanUtils.copyProperties(productRepository.findById(id).get(),productResponse);
    return productResponse;
  }

  @Override
  public void deleteProductById(Long id) {
    productRepository.deleteById(id);
  }

}
