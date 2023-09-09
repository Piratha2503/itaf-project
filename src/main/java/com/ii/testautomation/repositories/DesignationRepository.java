package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation,Long> {

    boolean existsByNameIgnoreCase(String designationName);
}
