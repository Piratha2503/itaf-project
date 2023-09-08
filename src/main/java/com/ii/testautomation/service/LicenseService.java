package com.ii.testautomation.service;

public interface LicenseService {

    boolean existsById(Long id);

    boolean isUpdateNameExists(String name,Long id);


}
