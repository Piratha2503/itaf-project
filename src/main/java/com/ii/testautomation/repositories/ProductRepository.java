package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Project, Long>{

}
