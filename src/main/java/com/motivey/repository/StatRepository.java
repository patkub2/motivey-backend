package com.motivey.repository;

import com.motivey.model.Stat;
import com.motivey.model.StatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends JpaRepository<Stat, StatId> {
    // Additional custom methods or queries can be added here if needed
}
