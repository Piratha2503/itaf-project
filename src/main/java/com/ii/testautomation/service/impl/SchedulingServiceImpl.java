package com.ii.testautomation.service.impl;

import com.ii.testautomation.dto.response.SchedulingResponse;
import com.ii.testautomation.entities.Scheduling;
import com.ii.testautomation.repositories.SchedulingRepository;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import com.ii.testautomation.service.SchedulingService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulingServiceImpl implements SchedulingService {

    @Autowired
    private SchedulingRepository schedulingRepository;
    @Override
    public List<SchedulingResponse> viewByProjectId(Long projectId, Pageable pageable, PaginatedContentResponse.Pagination pagination) {
        List<SchedulingResponse> schedulingResponseList = new ArrayList<>();
        Page<Scheduling> schedulingList = schedulingRepository.findByTestGrouping_ProjectId(pageable,projectId);
        pagination.setTotalRecords(schedulingList.getTotalElements());
        pagination.setTotalPages(schedulingList.getTotalPages());
        for (Scheduling scheduling : schedulingList)
        {
            SchedulingResponse schedulingResponse = new SchedulingResponse();
            BeanUtils.copyProperties(scheduling,schedulingResponse);
            schedulingResponseList.add(schedulingResponse);
        }
        return schedulingResponseList;
    }
}
