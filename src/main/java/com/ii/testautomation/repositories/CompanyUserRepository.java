package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long>, QuerydslPredicateExecutor<CompanyUser> {
    boolean existsByCompanyNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    boolean existsByContactNumber(String contactNumber);
}
