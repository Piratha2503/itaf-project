package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.entities.QScheduling;
import com.ii.testautomation.entities.QSubModules;
import com.ii.testautomation.repositories.SchedulingRepository;
import com.ii.testautomation.service.SchedulingService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulingServiceImpl implements SchedulingService {
    @Autowired
    private SchedulingRepository schedulingRepository;

}
