package com.motivey.initializer;

import com.motivey.model.ArmorTier;
import com.motivey.repository.ArmorTierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ArmorTierInitializer implements CommandLineRunner {


    @Autowired
    private ArmorTierRepository armorTierRepository;
    private static final Logger log = LoggerFactory.getLogger(ArmorTierInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        // Define the armor tiers
        if (armorTierRepository.count() == 0) {
            log.info("Initializing armor tiers...");
        List<ArmorTier> armorTiers = Arrays.asList(
                new ArmorTier(null, 1, 9, "Adventurer Cloths, Medieval"),
                new ArmorTier(null, 10, 29, "Leather Armour"),
                new ArmorTier(null, 30, 49, "Basic Iron Chainmail"),
                new ArmorTier(null, 50, 69, "Steel Plated Armor"),
                new ArmorTier(null, 70, Integer.MAX_VALUE, "Crystalline Vanguard Armour")
        );
        log.info("Armor tiers initialized.");

        // Save to the database
        armorTierRepository.saveAll(armorTiers);
    } else {
            log.info("Armor tiers already initialized.");
        }
    }

}

