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
 @Value("${message.success.save.project}")
 private String saveProjectSuccessMessage;

 @Value("${code.validation.project.alreadyExists}")
 private String projectAllReadyExistCode;

 @Value("${message.validation.project.alreadyExists}")
 private String projectAllReadyExistsMessage;



}
