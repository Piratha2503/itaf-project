package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Scheduling;
import com.ii.testautomation.response.common.PaginatedContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchedulingRepository extends JpaRepository<Scheduling,Long> {

    Page<Scheduling> findByTestGrouping_ProjectId(Pageable pageable, Long projectId);
}
