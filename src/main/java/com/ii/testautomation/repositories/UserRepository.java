package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByEmail(String email);
    boolean existsByStaffIdIgnoreCase(String staffId);

    List<Users> findByCompanyUserId(Long companyId);
}
