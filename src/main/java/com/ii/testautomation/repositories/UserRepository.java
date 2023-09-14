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

    Page<Users> findByCompanyUserId(Long id,BooleanBuilder booleanBuilder, Pageable pageable);

    Page<Users> findByCompanyUser_Id(BooleanBuilder booleanBuilder, Pageable pageable);

    Page<Users> findAllByCompanyUserId(Long id, Pageable pageable);
}