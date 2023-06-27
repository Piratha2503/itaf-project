package com.ii.testautomation.service.impl;

import com.ii.testautomation.repositories.TestCasesRepository;
import com.ii.testautomation.service.TestCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestCasesServiceImpl implements TestCasesService {
    @Autowired
    private TestCasesRepository testCasesRepository;
    @Override
    public boolean existsByTestCasesId(Long id) {
        return testCasesRepository.existsById(id);
    }
}
