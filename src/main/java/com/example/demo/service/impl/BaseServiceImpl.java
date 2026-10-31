package com.example.demo.service.impl;

import com.example.demo.entity.Base;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BaseRepository;
import com.example.demo.service.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BaseServiceImpl implements BaseService {

    private final BaseRepository baseRepository;

    public BaseServiceImpl(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Override
    @Transactional
    public Base createBase(String name, String location) {
        if (baseRepository.existsByName(name)) {
            throw new DuplicateResourceException("Base already exists with name: " + name);
        }
        return baseRepository.save(new Base(name, location));
    }

    @Override
    @Transactional(readOnly = true)
    public Base getBaseById(Long id) {
        return baseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Base> getAllBases() {
        return baseRepository.findAll();
    }

    @Override
    @Transactional
    public Base updateBase(Long id, String name, String location) {
        Base base = baseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Base not found with id: " + id));
        if (!base.getName().equals(name) && baseRepository.existsByName(name)) {
            throw new DuplicateResourceException("Base already exists with name: " + name);
        }
        base.setName(name);
        base.setLocation(location);
        return baseRepository.save(base);
    }

    @Override
    @Transactional
    public void deleteBase(Long id) {
        if (!baseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Base not found with id: " + id);
        }
        baseRepository.deleteById(id);
    }
}
