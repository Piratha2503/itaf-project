package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.SchedulingRequest;

import java.io.IOException;

import com.ii.testautomation.dto.response.ScheduleResponse;
import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.ii.testautomation.dto.request.SchedulingRequest;

public interface SchedulingService {
    void deleteScheduling(Long schedulingId);

    public ScheduleResponse getSchedulingById(Long id);
    public boolean existById(Long id);

    List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination);
    public void saveTestScheduling(SchedulingRequest SchedulingRequest);
    public void autoScheduling() throws IOException;
   boolean existsBySchedulingNameByTestGroupingAndProjectId(String name, Long projectId);
    void schedulingExecution(List<Long> testCaseIds, Long projectId, Long groupingId) throws IOException;
    public void updateScheduling(SchedulingRequest schedulingRequest);
    boolean isUpdateNameExists(String Name, Long SchedulingId);
}
