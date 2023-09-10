package com.ii.testautomation.service;

import com.ii.testautomation.dto.request.SchedulingRequest;
import com.ii.testautomation.dto.response.ScheduleResponse;
import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchedulingService {
    void deleteScheduling(Long schedulingId);

    ScheduleResponse getSchedulingById(Long id);

    boolean existById(Long id);

    List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination);

    void saveTestScheduling(SchedulingRequest SchedulingRequest);

    boolean existsBySchedulingNameByTestGroupingAndProjectId(String name, Long projectId);

    public void updateScheduling(SchedulingRequest schedulingRequest);

    boolean isUpdateNameExists(String Name, Long SchedulingId);

    boolean existsByTestCaseId(Long testCaseId);

    boolean existsByTestGroupingId(Long testGroupingId);
}
