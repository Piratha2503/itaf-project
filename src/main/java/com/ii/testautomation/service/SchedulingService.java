package com.ii.testautomation.service;

import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchedulingService {
    void deleteScheduling(Long schedulingId);

    public boolean existById(Long id);

    List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination);
}
