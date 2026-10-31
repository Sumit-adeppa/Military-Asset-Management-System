package com.example.demo.service;

import com.example.demo.entity.Base;
import java.util.List;

public interface BaseService {
    Base createBase(String name, String location);
    Base getBaseById(Long id);
    List<Base> getAllBases();
    Base updateBase(Long id, String name, String location);
    void deleteBase(Long id);
}
