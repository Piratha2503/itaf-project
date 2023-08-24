package com.ii.testautomation.service;
import com.ii.testautomation.dto.request.SchedulingRequest;
import java.io.IOException;

public interface SchedulingService {
   public void saveTestScheduling(SchedulingRequest SchedulingRequest);
public void autoScheduling();
   void schedulingExecution(Long schedulingId, Long projectId, Long groupingId) throws IOException;
}
