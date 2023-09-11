package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByEmail(String email);

    boolean existsByCompanyUserId(Long id);

    Users findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByContactNumberAndIdNot(String contactNumber, Long id);
}
