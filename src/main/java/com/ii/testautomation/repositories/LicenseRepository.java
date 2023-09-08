package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Licenses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<Licenses,Long> {
}
