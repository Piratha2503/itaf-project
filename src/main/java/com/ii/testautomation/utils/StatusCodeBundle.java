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







    @Value("${message.success.insertmain}")
    private String SuccessMessageInsert;
    @Value("${message.success.deletemain}")
    private String SuccessMessageDelete;
    @Value("${message.success.updatemain}")
    private String SuccessUpdateMessage;
    @Value("${message.success.viewallmain}")
    private String SuccessViewAllMessage;

    @Value("${message.failure.alreadyexistname}")
    private String NameAlreadyExist;
    @Value("${message.failure.alreadyexistpreix}")
    private String PrefixAlreadyExist;
    @Value("${message.failure.noid}")
    private String Noid;
    @Value("${message.failure.MainIdnotFound}")
    private String MainIdnotFound;

}
