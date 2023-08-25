package com.ii.testautomation.repositories;

import com.ii.testautomation.entities.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SequenceRepository extends JpaRepository<Sequence,Long> {
}
