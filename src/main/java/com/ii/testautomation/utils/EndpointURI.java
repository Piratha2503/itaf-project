package com.ii.testautomation.utils;

/*
 * Contains all the rest EndPoint URL constants
 */
public final class EndpointURI {
  private static final String BASE_API_PATH = "/api/v1/";
  private static final String SLASH = "/";
  private static final String SEARCH = "search";
  private static final String SEARCHWITHPAGE = "page";
  private static final String ID = "/{id}";
  
  // URLs for Project
  public static final String PROJECT = BASE_API_PATH + "project";
  public static final String PROJECT_IMPORT = BASE_API_PATH + "import";
  public static final String PROJECTS = BASE_API_PATH +SEARCH+SLASH + "products";
  public static final String PROJECT_BY_ID = PROJECT + ID;

  // URL for Main Modules
  public static final String MAINMODULE = BASE_API_PATH + "Mainmodule";
  public static final String MAINMODULE_IMPORT = BASE_API_PATH + "import";
  public static final String MAINMODULES = BASE_API_PATH +SEARCH+SLASH + "MainModules";
  public static final String MAINMODULEPAGE = BASE_API_PATH +SEARCHWITHPAGE+SLASH + "MainModules";
  public static final String MAINMODULE_BY_NAME = MAINMODULE+"/byname";
  public static final String MAINMODULE_BY_ID = MAINMODULE + ID;
  public static final String MAINMODULE_BY_MODULEID = BASE_API_PATH + "getByModuleId" + ID;
  // URLs for SubModules
  public static final String SUBMODULE = BASE_API_PATH + "subModules";
  public static final String SUBMODULE_IMPORT = BASE_API_PATH +SUBMODULE+ "import";
  public static final String SUBMODULES_SEARCH = BASE_API_PATH +SEARCH+SLASH + "subModules";
  public static final String SUBMODULE_BY_ID = SUBMODULE  + ID;
  public static final String SUBMODULE_BY_MAIN_MODULE_ID = SUBMODULE  + "/mainModuleId"+ID;

  // URLs for Test Types

 public static final String TESTTYPE = BASE_API_PATH + "Testtype";
 public static final String TESTTYPE_IMPORT = BASE_API_PATH +TESTTYPE+ "import";
 public static final String TESTTYPES_SEARCH = BASE_API_PATH +SEARCH+SLASH + "Testtypes";
 public static final String TESTTYPE_BY_ID = TESTTYPE  + ID;
   //URLs for modules
    public static final String MODULE = BASE_API_PATH + "module";
    public static final String MODULE_BY_ID = MODULE + ID;
    public static final String MODULES = BASE_API_PATH + SEARCH + SLASH + "modules";
    public static final String MODULES_BY_ID = BASE_API_PATH + "moduleByProjectId" + ID;
    // URLs for TestGrouping
    public static final String TEST_GROUPING = BASE_API_PATH + "testGrouping";
    public static final String TEST_GROUPING_IMPORT = BASE_API_PATH + TEST_GROUPING + "import";
    public static final String TEST_GROUPING_SEARCH = BASE_API_PATH + SEARCH + SLASH + "testGrouping";
    public static final String TEST_GROUPING_BY_ID = TEST_GROUPING + ID;
    public static final String TEST_GROUPING_BY_TEST_CASE_ID = TEST_GROUPING + "/testCase" + ID;
    public static final String TEST_GROUPING_BY_TEST_TYPE_ID = TEST_GROUPING + "/testType" + ID;

    //URLs for TestCases
    public static final String TESTCASE = BASE_API_PATH + "testcase";
    public static final String TESTCASE_BY_ID = TESTCASE + ID;
    public static final String TESTCASES = BASE_API_PATH + SEARCH + SLASH + "testcases";
    public static final String TESTCASES_BY_ID = BASE_API_PATH + "TestCaseBySubModuleId" + ID;


  private EndpointURI() {
  }
}
