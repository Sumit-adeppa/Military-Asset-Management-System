package com.example.demo.service.impl;

import com.example.demo.entity.Base;
import com.example.demo.entity.Personnel;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BaseRepository;
import com.example.demo.repository.PersonnelRepository;
import com.example.demo.service.PersonnelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonnelServiceImpl implements PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final BaseRepository baseRepository;

    public PersonnelServiceImpl(PersonnelRepository personnelRepository, BaseRepository baseRepository) {
        this.personnelRepository = personnelRepository;
        this.baseRepository = baseRepository;
    }

    @Override
    @Transactional
    public Personnel createPersonnel(String name, String rank, String serviceNumber, Long baseId) {
        if (personnelRepository.existsByServiceNumber(serviceNumber)) {
            throw new DuplicateResourceException("Personnel already exists with service number: " + serviceNumber);
        }
        Base base = baseRepository.findById(baseId)
                .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + baseId));
        return personnelRepository.save(new Personnel(name, rank, serviceNumber, base));
    }

    @Override
    @Transactional(readOnly = true)
    public Personnel getPersonnelById(Long id) {
        return personnelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personnel not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Personnel> getAllPersonnel() {
        return personnelRepository.findAllWithBase();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Personnel> getPersonnelByBase(Long baseId) {
        return personnelRepository.findByBaseId(baseId);
    }

    @Override
    @Transactional
    public Personnel updatePersonnel(Long id, String name, String rank, Long baseId) {
        Personnel personnel = personnelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personnel not found with id: " + id));
        Base base = baseRepository.findById(baseId)
                .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + baseId));
        personnel.setName(name);
        personnel.setRank(rank);
        personnel.setBase(base);
        return personnelRepository.save(personnel);
    }

    @Override
    @Transactional
    public void deletePersonnel(Long id) {
        if (!personnelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Personnel not found with id: " + id);
        }
        personnelRepository.deleteById(id);
    }
}
