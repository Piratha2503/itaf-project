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


//Modules
 @Value("${code.validation.module.alreadyExists}")
    private String moduleAllReadyExistsCode;

 @Value("${code.validation.module.notExists}")
 private  String moduleNotExistsCode;

 @Value("${message.success.update.module}")
 private String updateModuleSuccessMessage;

 @Value("${message.validation.module.name.alreadyExists}")
    private String moduleNameAllReadyExistsMessage;

 @Value("${message.success.save.module}")
    private String saveModuleSuccessMessage;

 @Value("${message.validation.module.prefix.alreadyExists}")
    private String modulePrefixAllReadyExistsMessage;

 @Value("${message.validation.module.notExists}")
    private String moduleNotExistsMessage;
 @Value("${message.success.delete.module}")
    private String deleteModuleSuccessMessage;
}
