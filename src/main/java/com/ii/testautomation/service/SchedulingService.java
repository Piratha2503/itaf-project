package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.SchedulingRequest;

public interface SchedulingService {
    public void saveTestScheduling(SchedulingRequest schedulingRequest);
    public void updateScheduling(SchedulingRequest schedulingRequest);
}
