package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.SchedulingRequest;

import java.io.IOException;

import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.ii.testautomation.dto.request.SchedulingRequest;

public interface SchedulingService {
    void deleteScheduling(Long schedulingId);

    public boolean existById(Long id);

    List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination);

    public void autoScheduling() throws IOException;


    void schedulingExecution(Long schedulingId, Long projectId, Long groupingId) throws IOException;


    boolean existsById( Long id);
    SchedulingResponse getSchedulingById(Long id);


    void schedulingExecution(List<Long> testCaseIds, Long projectId, Long groupingId) throws IOException;
    public void saveTestScheduling(SchedulingRequest schedulingRequest);
    public void updateScheduling(SchedulingRequest schedulingRequest);
    boolean isUpdateNameExists(String Name, Long SchedulingId);
}
