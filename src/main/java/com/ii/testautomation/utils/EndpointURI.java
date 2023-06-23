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
  public static final String PRODUCT = BASE_API_PATH + "product";
  public static final String PRODUCTS = BASE_API_PATH + "products";
  public static final String PRODUCT_BY_ID = PRODUCT + ID;


  private EndpointURI() {
  }
}
