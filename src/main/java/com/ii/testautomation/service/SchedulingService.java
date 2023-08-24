package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.SchedulingRequest;

import java.io.IOException;

import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchedulingService {

    List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination);

    public void saveTestScheduling(SchedulingRequest SchedulingRequest);

    public void autoScheduling();

    void schedulingExecution(Long schedulingId, Long projectId, Long groupingId) throws IOException;
}
