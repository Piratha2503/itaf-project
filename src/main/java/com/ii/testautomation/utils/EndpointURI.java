package com.ii.testautomation.utils;

/*
 * Contains all the rest EndPoint URL constants
 */
public final class EndpointURI {
    private static final String BASE_API_PATH = "/api/v1/";
    private static final String SLASH = "/";
    private static final String SEARCH = "search";
    private static final String SEARCH_WITH_PAGE = "page";
    private static final String ID = "/{id}";
    private static final String PROJECT_ID = "/{projectId}";

    // URLs for Project
    public static final String PROJECT = BASE_API_PATH + "project";
    public static final String PROJECT_FILE = PROJECT+"/updateFile";
    public static final String PROJECT_IMPORT = PROJECT + "/import";
    public static final String PROJECTS = BASE_API_PATH + SEARCH + SLASH + "products";
    public static final String PROJECT_BY_ID = PROJECT + ID;

    // URL for Main Modules
    public static final String MAIN_MODULE = BASE_API_PATH + "Mainmodule";
    public static final String MAIN_MODULE_IMPORT = BASE_API_PATH + "import";
    public static final String MAIN_MODULE_PAGE = BASE_API_PATH + SEARCH_WITH_PAGE + SLASH + "MainModules";
    public static final String MAIN_MODULE_BY_ID = MAIN_MODULE + ID;
    public static final String MAIN_MODULE_BY_MODULE_ID = BASE_API_PATH + "getByModuleId" + ID;
    public static final String MAIN_MODULE_BY_PROJECT_ID = BASE_API_PATH + "getByProjectId" + ID;

    // URLs for SubModules
    public static final String SUBMODULE = BASE_API_PATH + "subModules";
    public static final String SUBMODULE_IMPORT = SUBMODULE + "/import";
    public static final String SUBMODULES_SEARCH = BASE_API_PATH + SEARCH + SLASH + "subModules";
    public static final String SUBMODULE_BY_ID = SUBMODULE + ID;
    public static final String SUBMODULE_BY_MAIN_MODULE_ID = SUBMODULE + "/mainModuleId" + ID;
    public static final String SUBMODULE_BY_PROJECT_ID = SUBMODULE + "/project" + ID;

    //URLs for modules
    public static final String MODULE = BASE_API_PATH + "module";
    public static final String MODULE_BY_ID = MODULE + ID;
    public static final String MODULE_BY_PROJECT_ID = MODULE +"/project" +ID;
    public static final String MODULES = BASE_API_PATH + SEARCH + SLASH + "modules";
    public static final String MODULES_BY_ID = BASE_API_PATH + "moduleByProjectIdWithPagination" + ID;

    // URLs for Test Types
    public static final String TEST_TYPE = BASE_API_PATH + "testType";
    public static final String TEST_TYPE_IMPORT = BASE_API_PATH + TEST_TYPE + "import";
    public static final String TEST_TYPES_SEARCH = BASE_API_PATH + SEARCH + SLASH + "testTypes";
    public static final String TEST_TYPE_BY_ID = TEST_TYPE + ID;
    public static final String TEST_TYPE_BY_PROJECT_ID = BASE_API_PATH + "testTypeByProjectId" + ID;

    //URLs for TestCases
    public static final String TESTCASE = BASE_API_PATH + "testcase";
    public static final String TESTCASE_BY_ID = TESTCASE + ID;
    public static final String TESTCASES = BASE_API_PATH + SEARCH + SLASH + "testcases";
    public static final String TESTCASES_BY_ID = BASE_API_PATH + "TestCaseBySubModuleId" + ID;
    public static final String TESTCASE_IMPORT= TESTCASE+"/import"+ID;
    public static final String TESTCASE_BY_PROJECT_ID = BASE_API_PATH+"TestCaseByProjectIdWithPagination"+ID;
    public static final String TESTCASE_BY_MODULE_ID = BASE_API_PATH+"TestCaseByModuleId"+ID;
    public static final String TESTCASE_BY_MAIN_MODULE_ID = BASE_API_PATH+"TestCaseByMainModuleId"+ID;
    public static final String MODULE_IMPORT=MODULE +"/import";

    // URLs for TestGrouping
    public static final String TEST_GROUPING = BASE_API_PATH + "testGrouping";
    public static final String TEST_GROUPING_IMPORT = TEST_GROUPING + "/import";
    public static final String TEST_GROUPING_SEARCH = BASE_API_PATH + SEARCH + SLASH + "testGrouping";
    public static final String TEST_GROUPING_BY_ID = TEST_GROUPING + ID+PROJECT_ID;
    public static final String TEST_GROUPINGS_BY_ID = TEST_GROUPING + ID;
    public static final String TEST_GROUPINGS_EXCEL_BY_ID = TEST_GROUPING +"excel"+ID;
    public static final String TEST_GROUPING_BY_TEST_CASE_ID = TEST_GROUPING + "/testCase" + ID;
    public static final String TEST_GROUPING_BY_TEST_TYPE_ID = TEST_GROUPING + "/testType" + ID;
    public static final String TEST_GROUPING_BY_PROJECT_ID = TEST_GROUPING + "/project" + ID;
    public static final String TEST_GROUPING_UPDATE_EXECUTION_STATUS = TEST_GROUPING + "/update";

    // URLs for TestScenario
    public static final String TEST_SCENARIO = BASE_API_PATH + "testScenario";
    public static final String TEST_SCENARIO_BY_PROJECT_ID = BASE_API_PATH+"TestScenarioByProjectIdWithPagination"+ID;
    public static final String TEST_SCENARIO_BY_ID = TEST_SCENARIO + ID;
    public static final String TEST_SCENARIO_UPDATE_EXECUTION_STATUS = TEST_SCENARIO + "/update" + ID+PROJECT_ID;

    // Execution History
    public static final String EXECUTION_HISTORY = BASE_API_PATH + "ExecutionHistory";
    public static final String EXECUTION_HISTORY_BY_TEST_GROUPING_ID = EXECUTION_HISTORY+"/testGrouping"+ID;
    public static final String EXECUTION_HISTORY_BY_DATE = EXECUTION_HISTORY+"/Date"+ID;
    public static final String EXECUTION_HISTORY_ID = EXECUTION_HISTORY+ID;
    public static final String EXECUTION_HISTORY_PROJECT_ID = EXECUTION_HISTORY+ID+"/project"+PROJECT_ID;
    public static final String EXECUTION_HISTORY_DATE_FILTER = EXECUTION_HISTORY+"/DateFilter"+ID;
    public static final String EXECUTION_HISTORY_EMAIL = EXECUTION_HISTORY+"/email";

    // URLs for Scheduling History
    public static final String SCHEDULING_BY_ID = BASE_API_PATH + "scheduling"+ID;
    public static final String SCHEDULING_PROJECT_ID = BASE_API_PATH+"scheduling"+"/Project"+ID;
    public static final String TEST_SCHEDULING = BASE_API_PATH + "scheduling";
    public static final String SCHEDULES = BASE_API_PATH+"scheduling";

    public static final String CALCULATE_PROGRESS_PERCENTAGE = BASE_API_PATH + "calculateProgressPercentage";
    public static final String  USERS=BASE_API_PATH+"user";

    // Email Link
    public static final String EMAIL_lINK = "http://localhost:";

    // License
    public static final String LICENSE = BASE_API_PATH + "License";
    public static final String LICENSE_BY_ID = LICENSE + ID;

    //CompanyUser
    public static final String COMPANY_USERS=BASE_API_PATH+"companyUsers";
    public static final String COMPANY_USER_BY_ID = COMPANY_USERS+ID;

    //Designation
    public static final String DESIGNATION = BASE_API_PATH+"designation";

    private EndpointURI() {
    }
}
