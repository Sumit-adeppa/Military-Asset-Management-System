package com.example.demo.service;

import com.example.demo.entity.Personnel;
import java.util.List;

public interface PersonnelService {
    Personnel createPersonnel(String name, String rank, String serviceNumber, Long baseId);
    Personnel getPersonnelById(Long id);
    List<Personnel> getAllPersonnel();
    List<Personnel> getPersonnelByBase(Long baseId);
    Personnel updatePersonnel(Long id, String name, String rank, Long baseId);
    void deletePersonnel(Long id);
}
