package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>{

    boolean existsByName(String name);

    boolean existsByCode(String code);

}
