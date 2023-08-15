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
    @Value("${code.failure.file}")
    private String fileFailureCode;
    // Common File Failure Message
    @Value("${message.file.failure.common}")
    private String fileFailureMessage;
    @Value("${message.file.write.failure.common}")
    private String fileWriteFailureMessage;
    @Value("${message.jar.file.failure.common}")
    private String jarfileFailureMessage;
    @Value("${message.config.file.failure.common}")
    private String configFileFailureMessage;
    @Value("${message.validation.header.notExits}")
    private String headerNotExistsMessage;
    @Value("${message.failure.excelPath}")
    private String ExcelPathNotProvideMessage;
    @Value("${message.success.execution}")
    private String executionSuccessMessage;

    //Project Code
    @Value("${code.validation.project.alreadyExists}")
    private String projectAlReadyExistCode;
    @Value("${code.validation.project.notExists}")
    private String projectNotExistCode;
    @Value("${code.validation.project.dependent}")
    private String projectIdDependentCode;
    //Project Message
    @Value("${message.validation.project.notExits}")
    private String projectNotExistsMessage;
    @Value("${message.validation.project.dependent}")
    private String projectIdDependentMessage;
    @Value("${message.validation.project.code.alreadyExists}")
    private String projectCodeAlReadyExistMessage;
    @Value("${message.validation.project.Name.alreadyExists}")
    private String projectNameAlReadyExistMessage;
    @Value("${message.validation.project.file}")
    private String projectFileImportValidationMessage;
    @Value("${message.validation.project.file.name.empty}")
    private String projectNameEmptyMessage;
    @Value("${message.validation.project.file.empty}")
    private String projectFileEmptyMessage;
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
    @Value("${message.validation.save.project}")
    private String saveProjectValidationMessage;
    @Value("${message.validation.project.name.duplicate}")
    private String projectNameDuplicateMessage;
    @Value("${message.validation.project.code.duplicate}")
    private String projectCodeDuplicateMessage;
    @Value("${message.failure.project.configPath}")
    private String projectConfigPathNotProvideMessage;
    @Value("${message.failure.project.jarPath}")
    private String projectJarPathNotProvideMessage;

    //SubModules Code
    @Value("${code.validation.subModules.alreadyExists}")
    private String subModulesAlReadyExistCode;
    @Value("${code.validation.subModules.notExists}")
    private String subModulesNotExistCode;
    @Value("${code.validation.subModules.dependent}")
    private String subModulesDependentCode;
    //SubModules Message
    @Value("${message.validation.save.subModule}")
    private String saveSubModuleValidationMessage;
    @Value("${message.validation.subModule.file}")
    private String subModuleFileImportValidationMessage;
    @Value("${message.validation.subModules.file.empty}")
    private String subModulesFileEmptyMessage;
    @Value("${message.validation.subModule.file.name.empty}")
    private String subModuleNameEmptyMessage;
    @Value("${message.validation.subModule.file.prefix.empty}")
    private String subModulePrefixEmptyMessage;
    @Value("${message.validation.subModule.file.main_module_id.empty}")
    private String subModuleMainModuleIdEmptyMessage;
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
    @Value("${message.validation.get.project.notHave}")
    private String getSubModuleNotHaveProjectId;
    @Value("${message.validation.subModules.dependent}")
    private String subModulesDependentMessage;
    @Value("${message.validation.subModule.name.duplicate}")
    private String subModuleNameDuplicateMessage;
    @Value("${message.validation.subModule.prefix.duplicate}")
    private String subModulePrefixDuplicateMessage;
    @Value("${message.success.getSubModules.project}")
    private String subModulesByProjectId;
    //MainModules
    @Value("${code.validation.mainModules.notExists}")
    private String mainModulesNotExistCode;
    @Value("${code.validation.mainModules.alreadyExists}")
    private String AlreadyExistCode;

    // Main Modules messages
    @Value("${message.validation.mainModule.notExists}")
    private String mainModuleNotExistsMessage;
    @Value("${message.failure.module.Id.NotAssigned}")
    private String ModuleIdNotAssigned;
    @Value("${Message.validation.module.Id.AssignedWithAnotherTable}")
    private String IdAssignedWithAnotherTable;
    @Value("${code.validation.mainModule.dependent}")
    private String IdAssignedWithAnotherTableCode;
    @Value("${message.success.mainModules.save}")
    private String SuccessMessageInsertMainModules;
    @Value("${message.success.mainModules.delete}")
    private String SuccessMessageDeleteMainModules;
    @Value("${message.success.mainModules.update}")
    private String SuccessUpdateMessageMainModules;
    @Value("${message.success.mainModules.view}")
    private String SuccessViewAllMessageMainModules;
    @Value("${message.failure.mainModules.name.AlreadyExist}")
    private String mainModulesNameAlreadyExistMessage;
    @Value("${message.failure.mainModules.prefix.AlreadyExist}")
    private String mainModulesPrefixAlreadyExistMessage;
    @Value("${message.failure.module.Id.NotFound}")
    private String ModuleIdNotFound;
    @Value("${message.failure.mainModulesId.NotFound}")
    private String MainModulesIdNotFound;
    @Value("${message.validation.NotSave.mainModules}")
    private String mainModulesNotSavedMessage;
    @Value("${message.validation.mainModule.NameEmpty}")
    private String mainModulesNameFiledEmptyMessage;
    @Value("${message.validation.mainModule.PrefixEmpty}")
    private String mainModulesPrefixFiledEmptyMessage;
    @Value("${message.validation.mainModule.PrefixDuplicate}")
    private String mainModulesPrefixDuplicateMessage;
    @Value("${message.validation.mainModule.NameDuplicate}")
    private String mainModulesNameDuplicateMessage;
    @Value("${message.validation.mainModule.notMapped}")
    private String mainModulesNotMappedWithProjectMessage;
    //Modules Code
    @Value("${code.validation.module.alreadyExists}")
    private String moduleAlReadyExistsCode;
    @Value("${code.validation.module.notExists}")
    private String moduleNotExistsCode;
    @Value("${code.validation.module.dependent}")
    private String moduleDependentCode;
    //Modules Message
    @Value("${message.validation.module.file.empty}")
    private String moduleFileEmptyMessage;
    @Value("${message.success.update.module}")
    private String updateModuleSuccessMessage;
    @Value("${message.validation.module.file.prefix.empty}")
    private String modulePrefixEmptyMessage;
    @Value("${message.validation.module.file.name.empty}")
    private String moduleNameEmptyMessage;
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
    @Value("${message.validation.module.file.projectId.empty}")
    private String moduleProjectIdEmptyMessage;
    @Value("${message.validation.module.name.duplicate}")
    private String moduleNameDuplicateMessage;
    @Value("${message.validation.module.prefix.duplicate}")
    private String modulePrefixDuplicateMessage;
    @Value("${message.success.getAll.module.ByProjectId}")
    private String getAllModulesByProjectId;

    // Test Types Codes
    @Value("${code.validation.testType.alreadyExists}")
    private String TestTypeAlReadyExistCode;
    @Value("${code.validation.testTypes.notExists}")
    private String TestTypeNotExistCode;
    @Value("${code.validation.testTypes.notExists}")
    private String testTypesNotExistCode;
    @Value("${code.validation.testType.dependent}")
    private String testTypeDependentCode;

    //Test Type Messages
    @Value("${message.validation.testTypes.notExists}")
    private String testTypeNotExistMessage;
    @Value("${message.failure.alreadyExist.TestTypeId}")
    private String TestTypeIdAlReadyExistMessage;
    @Value("${message.failure.alreadyExist.TestTypename}")
    private String TestTypeNameAlReadyExistMessage;
    @Value("${message.failure.TestTypeId.NotFound}")
    private String TestTypeIdNotFoundMessage;
    @Value("${message.success.insert.TestTypes}")
    private String insertTestTypesSuccessMessage;
    @Value("${message.success.update.TestTypes}")
    private String updateTestTypeSuccessMessage;
    @Value("${message.success.view.TestTypeForId}")
    private String viewTestTypeforIdSuccessMessage;
    @Value("${message.success.getType.project}")
    private String viewTestTypeByProjectIdSuccessMessage;
    @Value("${message.success.viewAll.TestTypes}")
    private String viewAllTestTypesSuccessMessage;
    @Value("${message.success.delete.TestTypes}")
    private String deleteTestTypesSuccessMessage;
    @Value("${message.validation.testTypes.notExists}")
    private String testTypesNotExistsMessage;
    @Value("${message.validation.NotSave.TestTypes}")
    private String testTypesNotSavedMessage;
    @Value("${Message.validation.testType.Id.AssignedWithAnotherTable}")
    private String testTypeDependentMessage;
    @Value("${message.validation.testType.Name.Duplicate}")
    private String testTypeNameDuplicateMessage;
    @Value("${message.validation.testType.Name.Empty}")
    private String testTypeNameEmptyMessage;
    @Value("${message.validation.testType.Description.Empty}")
    private String testTypeDescriptionEmptyMessage;
    @Value("${message.validation.testType.DoesNot.mapped}")
    private String TestTypeNotMappedMessage;
    @Value("${message.success.getType.project}")
    private String testTypeByProjectId;

    //TestCases code
    @Value("${code.validation.testCases.notExists}")
    private String testCasesNotExistCode;
    @Value("${code.validation.testCases.alreadyExists}")
    private String testCasesAlreadyExistsCode;
    @Value("${code.validation.testcases.dependent}")
    private String testCasesDependentCode;
    //TestCase Message
    @Value("${message.validation.testcase.file.name.empty}")
    private String testCaseNameEmptyMessage;
    @Value("${message.validation.testCases.notExists}")
    private String testCasesNotExistsMessage;
    @Value("${message.validation.testCases.notExists.project}")
    private String testCasesAndProjectNotExistsSameProjectMessage;
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
    @Value("${message.validation.testcase.file.error}")
    private String testCaseFileErrorMessage;
    @Value("${message.validation.save.testcase}")
    private String testCaseValidationSaveMessage;
    @Value("${message.validation.testcase.name.duplicate}")
    private String testCaseNameDuplicateMessage;
    @Value("${message.validation.testcase.file.submoduleId.empty}")
    private String testcaseSubModuleIdEmptyMessage;
    @Value("${message.validation.testcase.file.empty}")
    private String testcaseFileEmptyMessage;
    @Value("${message.success.testcase.byProjectId}")
    private String getAllTestCasesSuccessGivenProjectId;
    @Value("${message.validation.testCase.getProject.notHave}")
    private String getTestCaseNotHaveProjectId;
    @Value("${message.validation.testCase.getByModule.notHave}")
    private String getTestCasesNotHaveModuleIdMessage;
    @Value("${message.validation.testCase.getByMainModule.notHave}")
    private String getTestCaseNotHaveMainModuleId;
    @Value("${message.success.testcase.byMainModuleId}")
    private String getAllTestCasesSuccessMainModuleIdMessage;
    @Value("${message.success.testcase.byModuleId}")
    private String getTestCasesByModuleIdSuccessMessage;
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
    @Value("${message.success.update.testGrouping}")
    private String updateTestGroupingSuccessMessage;
    @Value("${message.success.get.testGrouping}")
    private String getTestGroupingSuccessMessage;
    @Value("${message.success.getAll.testGrouping}")
    private String getAllTestGroupingSuccessMessage;
    @Value("${message.validation.testGrouping.file.empty}")
    private String testGroupingFileEmptyMessage;
    @Value("${message.validation.get.testGrouping.notHave.testCase}")
    private String getTestGroupingNotHaveTestCaseId;
    @Value("${message.validation.get.testGrouping.notHave.project}")
    private String getTestGroupingNotHaveProjectId;
    @Value("${message.validation.get.testGrouping.notHave.testType}")
    private String getTestGroupingNotHaveTestTypeId;
    @Value("${message.validation.delete.testGrouping}")
    private String deleteTestGroupingSuccessMessage;
    @Value("${message.validation.save.testGroup}")
    private String saveTestGroupValidationMessage;
    @Value("${message.validation.testGroup.file}")
    private String TestGroupFileImportValidationMessage;
    @Value("${message.validation.testGroup.file.name.empty}")
    private String TestGroupNameEmptyMessage;
    @Value("${message.validation.testGroup.file.test_case_id.empty}")
    private String TestGroupTestCaseIdEmptyMessage;
    @Value("${message.validation.testGroup.file.test_type_id.empty}")
    private String TestGroupTestTypeIdEmptyMessage;
    @Value("${message.validation.testGrouping.name.duplicate}")
    private String testGroupingNameDuplicateMessage;
    @Value("${message.success.getTestGrouping.project}")
    private String testGroupingByProjectId;
    @Value("${message.success.getTestGrouping.testType}")
    private String testGroupingByTestType;
    //TestScenario Codes
    @Value("${code.validation.testScenario.alreadyExists}")
    private String testScenariosAlreadyExistCode;
    @Value("${code.validation.testScenario.notExists}")
    private String testScenariosNotExistCode;

    //TestScenario Messages
    @Value("${message.success.insert.testScenario}")
    private String testScenariosSaveMessage;
    @Value("${message.failure.name.AlreadyExist.testScenario}")
    private String testScenariosNameAlreadyExistMessage;
    @Value("${message.failure.name.AlreadyExist.testCases.testScenario}")
    private String testCasesListAlreadyExistMessage;
}
