package com.ii.testautomation.utils;

/*
 * Contains all the rest EndPoint URL constants
 */
public final class EndpointURI {
    private static final String BASE_API_PATH = "/api/v1/";
    private static final String SLASH = "/";
    private static final String SEARCH = "search";
    private static final String ID = "/{id}";

    // URLs for Project
    public static final String PROJECT = BASE_API_PATH + "project";
    public static final String PROJECT_IMPORT = BASE_API_PATH + PROJECT + "import";
    public static final String PROJECTS = BASE_API_PATH + SEARCH + SLASH + "products";
    public static final String PROJECT_BY_ID = PROJECT + ID;

    // URLs for SubModules
    public static final String SUBMODULE = BASE_API_PATH + "subModules";
    public static final String SUBMODULE_IMPORT = BASE_API_PATH + SUBMODULE + "import";
    public static final String SUBMODULES_SEARCH = BASE_API_PATH + SEARCH + SLASH + "subModules";
    public static final String SUBMODULE_BY_ID = SUBMODULE + ID;
    public static final String SUBMODULE_BY_MAIN_MODULE_ID = SUBMODULE + "/mainModuleId" + ID;


    //URLs for modules
    public static final String MODULE = BASE_API_PATH + "module";
    public static final String MODULE_BY_ID = MODULE + ID;
    public static final String MODULES = BASE_API_PATH + SEARCH + SLASH + "modules";
    public static final String MODULES_BY_ID = BASE_API_PATH + "moduleByProjectId" + ID;


    //URLs for TestCases
    public static final String TESTCASE = BASE_API_PATH + "testcase";
    public static final String TESTCASE_BY_ID = TESTCASE + ID;
    public static final String TESTCASES = BASE_API_PATH + SEARCH + SLASH + "testcases";
    public static final String TESTCASES_BY_ID = BASE_API_PATH + "TestCaseBySubModuleId" + ID;


    private EndpointURI() {
    }
}
