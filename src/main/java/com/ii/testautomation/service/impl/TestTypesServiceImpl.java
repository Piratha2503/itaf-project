package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.TestTypesRepository;
import com.ii.testautomation.service.TestTypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestTypesServiceImpl implements TestTypesService {
    @Autowired
    private TestTypesRepository testTypesRepository;

    @Override
    public boolean existsByTestTypesId(Long id) {
        return testTypesRepository.existsById(id);
    }
}
