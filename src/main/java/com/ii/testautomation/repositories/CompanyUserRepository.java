package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long>, QuerydslPredicateExecutor<CompanyUser> {

    boolean existsByCompanyNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByContactNumber(String contactNumber);

    boolean existsByCompanyNameIgnoreCaseAndLicensesIdAndIdNot(String companyName, Long licensesId, Long id);

    boolean existsByEmailIgnoreCaseAndLicensesIdAndIdNot(String email, Long licensesId, Long id);

    boolean existsByContactNumberIgnoreCaseAndLicensesIdAndIdNot(String contactNumber, Long licensesId, Long id);

    boolean existsByLicensesId(Long id);

    CompanyUser findByEmail(String email);
}
