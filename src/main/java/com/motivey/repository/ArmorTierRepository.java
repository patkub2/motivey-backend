package com.motivey.repository;

import com.motivey.model.ArmorTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArmorTierRepository extends JpaRepository<ArmorTier, Long> {
    Optional<ArmorTier> findByMinLevelLessThanEqualAndMaxLevelGreaterThanEqual(int minLevel, int maxLevel);
}
