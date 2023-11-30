package com.motivey.service;

import com.motivey.model.ArmorTier;
import com.motivey.model.User;
import com.motivey.repository.ArmorTierRepository;
import com.motivey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArmorService {

    @Autowired
    private ArmorTierRepository armorTierRepository;

    @Autowired
    private UserRepository userRepository;

    public String getArmorIdForLevel(int level) {
        return armorTierRepository
                .findByMinLevelLessThanEqualAndMaxLevelGreaterThanEqual(level, level)
                .map(ArmorTier::getArmorId)
                .orElse("Adventurer Cloths, Medieval"); // or handle the case when no armor is found
    }

    public ArmorTier determineAppropriateArmor(int userLevel) {
        return armorTierRepository
                .findByMinLevelLessThanEqualAndMaxLevelGreaterThanEqual(userLevel, userLevel)
                .orElse(null); // or some default ArmorTier, or handle it as you prefer
    }

    public void updateArmorTier(User user) {
        ArmorTier appropriateArmor = determineAppropriateArmor(user.getCharacterLevel());
        if (!user.getCurrentArmor().equals(appropriateArmor)) {
            user.setCurrentArmor(appropriateArmor);
            // Save user with new armor tier
             userRepository.save(user);
        }

    }
}
