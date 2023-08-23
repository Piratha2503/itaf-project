package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchedulingRepository extends JpaRepository<Scheduling,Long> {
}
