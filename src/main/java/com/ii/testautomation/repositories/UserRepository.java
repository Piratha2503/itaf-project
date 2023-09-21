package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long>, QuerydslPredicateExecutor<Users> {
    boolean existsByEmailIgnoreCase(String email);

    List<Users> findByCompanyUserId(Long companyId);

    boolean existsByDesignationId(Long designationId);

    boolean existsByCompanyUserId(Long id);

    Users findByEmail(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByContactNumberIgnoreCaseAndIdNot(String contactNumber, Long id);

    boolean existsByContactNumberIgnoreCase(String contactNo);

    boolean existsByPassword(String password);

    boolean existsByStatus(String status);

    List<Users> findByFirstNameIgnoreCase(String firstName);

    List<Users> findByLastNameIgnoreCase(String firstName);

    Users findByCompanyUserIdAndDesignationName(Long id, String companyAdmin);
}