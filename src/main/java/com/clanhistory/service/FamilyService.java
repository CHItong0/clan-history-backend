package com.clanhistory.service;

import com.clanhistory.entity.Family;
import java.util.List;

public interface FamilyService {
    List<Family> findAll();
    Family findById(Long id);
    boolean save(Family family);
    boolean updateById(Family family);
    boolean deleteById(Long id);
    List<Family> findByDeleted(Integer deleted);
}
