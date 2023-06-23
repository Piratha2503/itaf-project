package com.ii.testautomation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ii.testautomation.dto.request.ProductRequest;
import com.ii.testautomation.enums.RequestStatus;
import com.ii.testautomation.response.common.BaseResponse;
import com.ii.testautomation.response.common.ContentResponse;
import com.ii.testautomation.service.ProductService;
import com.ii.testautomation.utils.Constants;
import com.ii.testautomation.utils.EndpointURI;
import com.ii.testautomation.utils.StatusCodeBundle;

@RestController
@CrossOrigin
public class ProductController {
  @Autowired
  private ProductService productService;

  @Autowired
  private StatusCodeBundle statusCodeBundle;

  @GetMapping(value = EndpointURI.PRODUCTS)
  public ResponseEntity<Object> getAllGroups() {
    return ResponseEntity
        .ok(new ContentResponse<>(Constants.PRODUCTS, productService.getAllProducts(),
            RequestStatus.SUCCESS.getStatus(), statusCodeBundle.getCommonSuccessCode(),
            statusCodeBundle.getGetAllProductSuccessMessage()));
  }

  @PostMapping(value = EndpointURI.PRODUCT)
  public ResponseEntity<Object> saveGuide(@RequestBody ProductRequest productRequest) {
    productService.createProduct(productRequest);
    return ResponseEntity.ok(new BaseResponse(RequestStatus.SUCCESS.getStatus(),
        statusCodeBundle.getCommonSuccessCode(), statusCodeBundle.getSaveProductSuccessMessage()));
  }

}
