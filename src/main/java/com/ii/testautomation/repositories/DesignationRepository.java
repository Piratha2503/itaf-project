package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

  boolean existsByNameIgnoreCase(String designationName);

  List<Designation> findAllDesignationByCompanyUserId(Long id);

  boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

  Designation findDistinctByNameAndCompanyUserId(String name, Long id);
}
