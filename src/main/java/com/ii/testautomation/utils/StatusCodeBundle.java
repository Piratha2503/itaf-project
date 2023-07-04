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

    //Project Code
    @Value("${code.validation.project.alreadyExists}")
    private String projectAlReadyExistCode;
    @Value("${code.validation.project.notExists}")
    private String projectNotExistCode;
    @Value("${code.validation.project.file.empty}")
    private String projectFileEmptyCode;
    @Value("${code.validation.project.dependent}")
    private String projectIdDependentCode;
    @Value("${code.validation.project.alreadyExists}")
    private String AlreadyExistCode;
    //Project Message
    @Value("${message.validation.project.notExits}")
    private String projectNotExistsMessage;
    @Value("${message.validation.project.dependent}")
    private String projectIdDependentMessage;
    @Value("${message.validation.project.code.alreadyExists}")
    private String projectCodeAlReadyExistMessage;
    @Value("${message.validation.project.Name.alreadyExists}")
    private String projectNameAlReadyExistMessage;
    @Value("${message.validation.project.file.error}")
    private String projectFileErrorMessage;
    @Value("${message.success.project.file}")
    private String projectFileUploadSuccessMessage;
    @Value("${message.validation.project.file}")
    private String projectFileUploadValidationMessage;
    @Value("${message.validation.file.csv}")
    private String fileCsvValidationMessage;
    @Value("${message.validation.project.file.name.empty}")
    private String projectNameEmptyMessage;
    @Value("${message.validation.project.file.code.empty}")
    private String projectCodeEmptyMessage;
    @Value("${message.validation.project.file.description.empty}")
    private String projectDescriptionEmptyMessage;
    @Value("${message.success.save.project}")
    private String saveProjectSuccessMessage;
    @Value("${message.validation.save.project}")
    private String saveProjectValidationMessage;
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
    @Value("${message.failure.alreadyexistname}")
    private String NameAlreadyExist;
    @Value("${message.failure.alreadyexistpreix}")
    private String PrefixAlreadyExist;
    @Value("${message.failure.noid}")
    private String ModuleIdNotFound;
    @Value("${message.failure.MainIdnotFound}")
    private String MainIdnotFound;
    @Value("${message.failure.moduleidnotassignedwithMainmod}")
    private String ModuleIdNotAssigned;
    @Value("${Message.validation.IdAsignedwithAnotherTable}")
    private String IdAsignedwithanotherTable;
    @Value("${message.success.insertmain}")
    private String SuccessMessageInsert;
    @Value("${message.success.deletemain}")
    private String SuccessMessageDelete;
    @Value("${message.success.updatemain}")
    private String SuccessUpdateMessage;
    @Value("${message.success.viewallmain}")
    private String SuccessViewAllMessage;
    //Modules Code
    @Value("${code.validation.module.alreadyExists}")
    private String moduleAlReadyExistsCode;
    @Value("${code.validation.module.notExists}")
    private String moduleNotExistsCode;
    @Value("${message.success.update.module}")
    private String updateModuleSuccessMessage;
    //Modules Message
    @Value("${message.validation.module.file.prefix.empty}")
    private String moduleNameEmptyMessage;
    @Value("${message.validation.module.file.name.empty}")
    private String modulePrefixEmptyMessage;
    @Value("${message.validation.module.name.alreadyExists}")
    private String moduleNameAlReadyExistsMessage;
    @Value("${message.success.save.module}")
    private String saveModuleSuccessMessage;
    @Value("${message.validation.module.prefix.alreadyExists}")
    private String modulePrefixAlReadyExistsMessage;
    @Value("${message.validation.module.notExists}")
    private String moduleNotExistsMessage;
    @Value("${message.validation.module.notHaveProject}")
    private String moduleNotHaveProjectMessage;
    @Value("${message.success.delete.module}")
    private String deleteModuleSuccessMessage;
    @Value("${message.success.getAll.module}")
    private String getAllModuleSuccessMessage;
    @Value("${message.success.getById.module}")
    private String getModuleByIdSuccessMessage;
    @Value("${message.success.getByProjectId.module}")
    private String getModuleByProjectIdSuccessMessage;
    @Value("${message.validation.module.assigned}")
    private String getValidationModuleAssignedMessage;
    @Value("${message.validation.save.module}")
    private String saveModuleValidationMessage;
    @Value("${message.validation.module.file.error}")
    private String moduleFileErrorMessage;

    // Test Types Codes & Messages //
    @Value("${code.validation.testtype.alreadyExists}")
    private String TestTypeAlReadyExistCode;
    @Value("${code.validation.testtype.notExists}")
    private String TestTypeNotExistCode;
    @Value("${message.failure.alreadyexistTestTypeid}")
    private String TestTypeIdAlReadyExistMessage;
    @Value("${message.failure.alreadyexistTestTypename}")
    private String TestTypeNameAlReadyExistMessage;
    @Value("${message.failure.TestTypeidNotFound}")
    private String TestTypeIdNotFoundMessage;
    @Value("${message.success.insertTestTypes}")
    private String insertTestTypesSuccessMessage;
    @Value("${message.success.updateTestTypes}")
    private String updateTestTypeSuccessMessage;
    @Value("${message.success.viewTestTypeforId}")
    private String viewTestTypeforIdSuccessMessage;
    @Value("${message.success.viewallTestTypes}")
    private String viewAllTestTypesSuccessMessage;
    @Value("${message.success.deleteTestTypes}")
    private String deleteTestTypesSuccessMessage;

    // Bulk Code & Msg
    @Value("${code.bulkimport.success}")
    private String BulkImportCode;
    @Value("${message.bulkimport.success}")
    private String BulkImportMessage;

    /*
     = Module Id Does not Assigned in Main Modules

     */


    //SubModules Code

    @Value("${code.validation.subModules.dependent}")
    private String subModulesDependentCode;
    //SubModules Message
    @Value("${message.validation.subModules.dependent}")
    private String subModulesDependentMessage;


    //TestCases code
    @Value("${code.validation.testCases.notExists}")
    private String testCasesNotExistCode;
    @Value("${code.validation.testCases.alreadyExists}")
    private String testCasesAlreadyExistsCode;
    //TestCase Message
    @Value("${message.validation.testCases.notExists}")
    private String testCasesNotExistsMessage;
    @Value("${message.validation.testCases.name.alreadyExists}")
    private String testCaseNameAlreadyExistsMessage;
    @Value("${message.success.save.testcases}")
    private String saveTestCaseSuccessMessage;
    @Value("${message.success.getById.testCase}")
    private String getTestCaseByIdSuccessMessage;
    @Value("${message.success.delete.testCase}")
    private String deleteTestCaseSuccessMessage;
    @Value("${message.success.update.testCase}")
    private String updateTestCaseSuccessMessage;
    @Value("${message.success.getAll.testCases}")
    private String getAllTestCasesSuccessMessage;
    @Value("${message.success.getBySubModuleId.testCase}")
    private String getTestCaseBySubModuleIdSuccessMessage;
    @Value("${message.validation.testCase.notHaveSubModule}")
    private String getTestCaseNotHaveSubModuleIdMessage;
    @Value("${message.validation.testCase.assigned}")
    private String getValidationTestCaseAssignedMessage;

    //TestTypes Code
    @Value("${code.validation.testTypes.notExists}")
    private String testTypesNotExistCode;
    //TestType Message
    @Value("${message.validation.testTypes.notExists}")
    private String testTypesNotExistsMessage;

    //TestGrouping Code
    @Value("${code.validation.testGrouping.alreadyExists}")
    private String testGroupingAlReadyExistCode;
    @Value("${code.validation.testGrouping.notExists}")
    private String testGroupingNotExistCode;
    //TestGrouping Message
    @Value("${message.validation.testGrouping.Name.alreadyExists}")
    private String testGroupingNameAlReadyExistMessage;
    @Value("${message.validation.testGrouping.notExists}")
    private String testGroupingNotExistsMessage;
    @Value("${message.success.save.testGrouping}")
    private String saveTestGroupingSuccessMessage;
    @Value("${message.success.get.testGrouping}")
    private String getTestGroupingSuccessMessage;
    @Value("${message.success.getAll.testGrouping}")
    private String getAllTestGroupingSuccessMessage;
    @Value("${message.validation.get.testGrouping.notHave.testCase}")
    private String getTestGroupingNotHaveTestCaseId;
    @Value("${message.validation.get.testGrouping.notHave.testType}")
    private String getTestGroupingNotHaveTestTypeId;
    @Value("${message.validation.delete.testGrouping}")
    private String deleteTestGroupingSuccessMessage;
}
