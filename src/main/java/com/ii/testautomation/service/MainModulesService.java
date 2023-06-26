package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.entities.MainModules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MainModulesService
{
   public void insertMainModules(MainModulesRequest mainModulesRequest);
   public void deleteMainModules(Long id);
   public MainModules getByMainModulesId(Long id);
   public void updateMainModules(MainModulesRequest mainModulesRequest);
   public Page<MainModules> viewAllMainModulesPage(Pageable pageable);
   public boolean isExistMainModulesId(Long id);
   public boolean isExistMainModulesName(String name);
   public boolean isExistPrefix(String prefix);
   public boolean isExistModulesId(Long id);
   public boolean isUpdateMainModulesNameExist(String mainModuleName, Long mainModuleId);
   public boolean isUpdateMainModulesPrefixExist(String mainModuleprefix, Long mainModuleId);
}
