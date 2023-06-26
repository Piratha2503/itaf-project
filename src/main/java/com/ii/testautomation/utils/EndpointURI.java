package com.ii.testautomation.utils;

/*
 * Contains all the rest EndPoint URL constants
 */
public final class EndpointURI {
  private static final String BASE_API_PATH = "/api/v1/";
  private static final String SLASH = "/";
  private static final String SEARCH = "search";
  private static final String ID = "/{id}";
  
  // URLs for Product
  public static final String PROJECT = BASE_API_PATH + "project";
  public static final String PROJECT_IMPORT = BASE_API_PATH + "import";
  public static final String PROJECTS = BASE_API_PATH +SEARCH+SLASH + "products";
  public static final String PROJECT_BY_ID = PROJECT + ID;















  //URLs for modules
  public static final String MODULE =BASE_API_PATH +"module";
  public static final String MODULE_BY_ID =MODULE +ID;


  private EndpointURI() {
  }
}
