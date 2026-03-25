package com.clanhistory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.entity.Family;
import com.clanhistory.mapper.FamilyMapper;
import com.clanhistory.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyServiceImpl implements FamilyService {

    private final FamilyMapper familyMapper;

    @Override
    public List<Family> findAll() {
        return familyMapper.selectList(null);
    }

    @Override
    public Family findById(Long id) {
        return familyMapper.selectById(id);
    }

    @Override
    public boolean save(Family family) {
        return familyMapper.insert(family) > 0;
    }

    @Override
    public boolean updateById(Family family) {
        return familyMapper.updateById(family) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return familyMapper.deleteById(id) > 0;
    }

    @Override
    public List<Family> findByDeleted(Integer deleted) {
        return familyMapper.selectList(new LambdaQueryWrapper<Family>().eq(Family::getDeleted, deleted));
    }
}
