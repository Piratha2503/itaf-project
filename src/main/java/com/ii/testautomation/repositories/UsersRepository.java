package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);

    List<Users> findByCompanyUserId(Long companyId);

    boolean existsByCompanyUserId(Long id);

    boolean existsByContactNumber(String contactNo);
}
