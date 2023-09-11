package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByEmail(String email);
   // boolean existsByStaffIdIgnoreCase(String staffId);

    boolean existsByDesignationId(Long designationId);
}
