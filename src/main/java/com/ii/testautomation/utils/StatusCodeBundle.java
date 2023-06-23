package com.ii.testautomation.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Specifying custom messages
 */
@Component
@PropertySource("classpath:MessagesAndCodes.properties")
@Getter
@Setter
public class StatusCodeBundle {
  // Common Success code
  @Value("${code.success.common}")
  private String commonSuccessCode;

  @Value("${code.failure.common}")
  private String failureCode;

//Product
 @Value("${message.success.save.product}")
 private String saveProductSuccessMessage;

 @Value("${message.success.update.product}")
 private String updateProductSuccessMessage;

 @Value("${message.success.getAll.product}")
 private String getAllProductSuccessMessage;

 @Value("${message.success.delete.product}")
 private String deleteProductSuccessMessage;

 @Value("${message.success.getById.product}")
 private String getByIdProductSuccessMessage;

 @Value("${code.validation.product.notExists}")
 private String productNotExistCode;

 @Value("${message.validation.product.notExists}")
 private String productNotExistMessage;

 @Value("${code.validation.product.alreadyExists}")
 private String productAlreadyExistCode;

 @Value("${message.validation.product.alreadyExists}")
 private String productAlreadyExistMessage;

}
