package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
