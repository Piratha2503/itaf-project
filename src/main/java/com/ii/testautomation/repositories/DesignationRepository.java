package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface DesignationRepository extends JpaRepository<Designation, Long>, QuerydslPredicateExecutor<Designation> {

    boolean existsByNameIgnoreCase(String designationName);
}
