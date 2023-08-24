package com.ii.testautomation.service;

import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.dto.response.TestCaseResponse;

import java.util.List;

public interface SchedulingService {


    boolean existsById( Long id);
    SchedulingResponse getSchedulingById(Long id);


}
