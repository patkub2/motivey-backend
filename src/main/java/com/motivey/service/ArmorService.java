package com.motivey.service;

import com.motivey.model.ArmorTier;
import com.motivey.repository.ArmorTierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArmorService {

    @Autowired
    private ArmorTierRepository armorTierRepository;

    public String getArmorIdForLevel(int level) {
        return armorTierRepository
                .findByMinLevelLessThanEqualAndMaxLevelGreaterThanEqual(level, level)
                .map(ArmorTier::getArmorId)
                .orElse("defaultArmorId"); // or handle the case when no armor is found
    }
}
