package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
    boolean existsByCompanyNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    boolean existsByContactNumber(String contactNumber);

    boolean existsByCompanyNameIgnoreCaseAndLicensesIdAndIdNot(String companyName, Long licensesId, Long id);

    boolean existsByEmailIgnoreCaseAndLicensesIdAndIdNot(String email, Long licensesId, Long id);

    boolean existsByContactNumberIgnoreCaseAndLicensesIdAndIdNot(String contactNumber, Long licensesId, Long id);

}
