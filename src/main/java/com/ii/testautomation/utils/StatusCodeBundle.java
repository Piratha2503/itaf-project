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
    @Value("${code.validation.project.alreadyExists}")
    private String projectAlReadyExistCode;
    @Value("${code.validation.project.notExists}")
    private String projectNotExistCode;
    @Value("${code.validation.project.file.empty}")
    private String projectFileEmptyCode;
    @Value("${message.validation.project.notExits}")
    private String projectNotExistsMessage;
    @Value("${message.validation.project.code.alreadyExists}")
    private String projectCodeAlReadyExistMessage;
    @Value("${message.validation.project.Name.alreadyExists}")
    private String projectNameAlReadyExistMessage;
    @Value("${message.validation.project.alreadyExists}")
    private String projectAlReadyExistMessage;
    @Value("${message.validation.project.file.name.empty}")
    private String projectNameEmptyMessage;
    @Value("${message.validation.project.file.code.empty}")
    private String projectCodeEmptyMessage;
    @Value("${message.validation.project.file.description.empty}")
    private String projectDescriptionEmptyMessage;
    @Value("${message.success.save.project}")
    private String saveProjectSuccessMessage;
    @Value("${message.success.update.project}")
    private String updateProjectSuccessMessage;
    @Value("${message.success.getAll.project}")
    private String getAllProjectSuccessMessage;
    @Value("${message.success.get.project}")
    private String getProjectSuccessMessage;
    @Value("${message.success.delete.project}")
    private String getProjectDeleteMessage;








//Modules
 @Value("${code.validation.module.alreadyExists}")
    private String moduleAlReadyExistsCode;

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
    @Value("${message.validation.module.notHaveProject}")
    private String moduleNotHaveProjectMessage;
 @Value("${message.success.delete.module}")
    private String deleteModuleSuccessMessage;
 @Value("${message.success.getAll.module}")
    private String getAllModuleSuccessMessage;

 @Value("{message.success.getById.module}")
    private  String getModuleByIdSuccessMessage;
 @Value("${message.success.getByProjectId.module}")
    private String getModuleByProjectIdSuccessMessage;
}
