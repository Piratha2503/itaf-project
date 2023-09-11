package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.CompanyUser;
import com.ii.testautomation.entities.Designation;
import com.ii.testautomation.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<Users,Long> {
//    boolean existsByEmail(String email);
//    boolean existsByStaffIdIgnoreCase(String staffId);



    boolean existsByEmail(String email);
    @Query("SELECT u.designation FROM Users u WHERE u.companyUser = :companyUser")
    List<Designation> findByCompanyUser(@Param("companyUser")CompanyUser companyUser);

    List<Users> findByDesignation(Designation designation);

    List<Users> findByCompanyUserId(Long companyId);
}
