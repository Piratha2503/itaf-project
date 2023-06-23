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
  public static final String PROJECTS = BASE_API_PATH + "products";
  public static final String PRODUCT_BY_ID = PROJECT + ID;


  private EndpointURI() {
  }
}
