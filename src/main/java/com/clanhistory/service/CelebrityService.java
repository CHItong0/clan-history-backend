package com.clanhistory.service;

import com.clanhistory.entity.Celebrity;
import java.util.List;

public interface CelebrityService {
    List<Celebrity> findAll();
    Celebrity findById(Long id);
    boolean save(Celebrity celebrity);
    boolean updateById(Celebrity celebrity);
    boolean deleteById(Long id);
    List<Celebrity> findByFamilyId(Long familyId);
    List<Celebrity> findByDeleted(Integer deleted);
}
