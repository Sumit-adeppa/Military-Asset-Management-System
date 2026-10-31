package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.enums.*;
import com.example.demo.repository.*;
import com.example.demo.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.data-initializer.enabled", havingValue = "true", matchIfMissing = true)
public class DataInitializer implements CommandLineRunner {

    private final BaseRepository baseRepository;
    private final EquipmentTypeRepository equipmentTypeRepository;
    private final UserRepository userRepository;
    private final PersonnelRepository personnelRepository;
    private final InventoryRepository inventoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${DEFAULT_ADMIN_PASSWORD:admin123}")
    private String defaultAdminPassword;

    @Value("${DEFAULT_COMMANDER_PASSWORD:commander123}")
    private String defaultCommanderPassword;

    @Value("${DEFAULT_LOGISTICS_PASSWORD:logistics123}")
    private String defaultLogisticsPassword;

    public DataInitializer(BaseRepository baseRepository,
                          EquipmentTypeRepository equipmentTypeRepository,
                          UserRepository userRepository,
                          PersonnelRepository personnelRepository,
                          InventoryRepository inventoryRepository,
                          PasswordEncoder passwordEncoder) {
        this.baseRepository = baseRepository;
        this.equipmentTypeRepository = equipmentTypeRepository;
        this.userRepository = userRepository;
        this.personnelRepository = personnelRepository;
        this.inventoryRepository = inventoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        Base baseA = baseRepository.save(new Base("Alpha Base", "Location A"));
        Base baseB = baseRepository.save(new Base("Bravo Base", "Location B"));
        Base baseC = baseRepository.save(new Base("Charlie Base", "Location C"));

        EquipmentType ammo = equipmentTypeRepository.save(
                new EquipmentType("5.56mm Ammunition", "Ammunition", SerializationType.QUANTITY));
        EquipmentType rifle = equipmentTypeRepository.save(
                new EquipmentType("M4 Carbine", "Weapon", SerializationType.SERIALIZED));
        EquipmentType humvee = equipmentTypeRepository.save(
                new EquipmentType("Humvee", "Vehicle", SerializationType.SERIALIZED));
        EquipmentType rations = equipmentTypeRepository.save(
                new EquipmentType("MRE Rations", "Supply", SerializationType.QUANTITY));

        User admin = userRepository.save(
                new User("admin", passwordEncoder.encode(defaultAdminPassword), Role.ADMIN, null));
        userRepository.save(
                new User("cmdrA", passwordEncoder.encode(defaultCommanderPassword), Role.BASE_COMMANDER, baseA));
        userRepository.save(
                new User("cmdrB", passwordEncoder.encode(defaultCommanderPassword), Role.BASE_COMMANDER, baseB));
        userRepository.save(
                new User("logistics", passwordEncoder.encode(defaultLogisticsPassword), Role.LOGISTICS_OFFICER, null));

        personnelRepository.save(new Personnel("John Smith", "SGT", "SN001", baseA));
        personnelRepository.save(new Personnel("Jane Doe", "LT", "SN002", baseA));
        personnelRepository.save(new Personnel("Bob Johnson", "CPL", "SN003", baseB));
        personnelRepository.save(new Personnel("Alice Brown", "MAJ", "SN004", baseC));

        inventoryRepository.save(new Inventory(baseA, ammo, 5000));
        inventoryRepository.save(new Inventory(baseA, rifle, 200));
        inventoryRepository.save(new Inventory(baseA, humvee, 50));
        inventoryRepository.save(new Inventory(baseA, rations, 1000));
        inventoryRepository.save(new Inventory(baseB, ammo, 3000));
        inventoryRepository.save(new Inventory(baseB, rifle, 150));
        inventoryRepository.save(new Inventory(baseB, humvee, 30));
        inventoryRepository.save(new Inventory(baseC, ammo, 2000));
        inventoryRepository.save(new Inventory(baseC, rations, 500));

    }
}
