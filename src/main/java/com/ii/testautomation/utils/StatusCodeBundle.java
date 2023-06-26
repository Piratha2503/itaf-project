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

//Project
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
    private String deleteProjectSuccessMessage;
    //SubModules
    @Value("${code.validation.subModules.alreadyExists}")
    private String subModulesAlReadyExistCode;
    @Value("${code.validation.subModules.notExists}")
    private String subModulesNotExistCode;

    @Value("${message.validation.subModule.Name.alreadyExists}")
    private String subModuleNameAlReadyExistMessage;
    @Value("${message.validation.subModule.prefix.alreadyExists}")
    private String subModulePrefixAlReadyExistMessage;
    @Value("${message.validation.subModule.notExists}")
    private String subModuleNotExistsMessage;
    @Value("${message.success.save.subModule}")
    private String saveSubModuleSuccessMessage;
    @Value("${message.success.update.subModule}")
    private String updateSubModuleSuccessMessage;
    @Value("${message.success.get.subModules}")
    private String getSubModulesSuccessMessage;
    @Value("${message.success.getAll.subModule}")
    private String getAllSubModuleSuccessMessage;
    @Value("${message.success.delete.subModule}")
    private String deleteSubModuleSuccessMessage;
    @Value("${message.validation.get.mainModule.notHave}")
    private String getSubModuleNotHaveMainModuleId;
    //MainModules
    @Value("${code.validation.mainModules.notExists}")
    private String mainModulesNotExistCode;

    @Value("${message.validation.mainModule.notExists}")
    private String mainModuleNotExistsMessage;


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
