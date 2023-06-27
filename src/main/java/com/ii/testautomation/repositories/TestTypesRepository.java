package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.TestTypes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTypesRepository extends JpaRepository<TestTypes, Long> {
}
