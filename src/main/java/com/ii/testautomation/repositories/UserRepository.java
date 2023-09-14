package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Users;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long>, QuerydslPredicateExecutor<Users> {
    boolean existsByEmailIgnoreCase(String email);

    List<Users> findByCompanyUserId(Long companyId);

    boolean existsByDesignationId(Long designationId);

    boolean existsByCompanyUserId(Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByContactNumberIgnoreCaseAndIdNot(String contactNumber, Long id);

    boolean existsByContactNumberIgnoreCase(String contactNo);

//    Page<Users> findByCompanyUser_Id(Long companyUserId, Pageable pageable,BooleanBuilder booleanBuilder);

    //Page<Users> findByCompanyUser_Id(BooleanBuilder booleanBuilder, Pageable pageable);

    Page<Users> findAllByCompanyUserId(Long companyUserid, Pageable pageable,BooleanBuilder booleanBuilder);

    Users findByEmail(String email);

    List<Users> findByCompanyUser_Id(Long companyUserId);



}