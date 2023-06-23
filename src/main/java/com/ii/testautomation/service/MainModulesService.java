package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.MainModulesRequest;
import com.ii.testautomation.entities.MainModules;
import com.ii.testautomation.entities.Modules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MainModulesService
{
   public void insertMain(MainModulesRequest mainModulesRequest);
   public void deleteMain(Long id);
   public MainModules getById(Long id);
   public void updateMod(MainModulesRequest mainModulesRequest);
   public Page<MainModules> ViewAll(Pageable pageable);
   public boolean isExistModId(Long id);
   public boolean isExistMainName(String name);
   public boolean isExistPrefix(String prefix);
   public boolean isExistMainModId(Long id);
}
