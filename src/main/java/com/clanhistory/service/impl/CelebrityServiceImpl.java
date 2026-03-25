package com.clanhistory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.entity.Celebrity;
import com.clanhistory.mapper.CelebrityMapper;
import com.clanhistory.service.CelebrityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CelebrityServiceImpl implements CelebrityService {

    private final CelebrityMapper celebrityMapper;

    @Override
    public List<Celebrity> findAll() {
        return celebrityMapper.selectList(null);
    }

    @Override
    public Celebrity findById(Long id) {
        return celebrityMapper.selectById(id);
    }

    @Override
    public boolean save(Celebrity celebrity) {
        celebrity.setDeleted(0);
        return celebrityMapper.insert(celebrity) > 0;
    }

    @Override
    public boolean updateById(Celebrity celebrity) {
        celebrity.setDeleted(0);
        return celebrityMapper.updateById(celebrity) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return celebrityMapper.deleteById(id) > 0;
    }

    @Override
    public List<Celebrity> findByFamilyId(Long familyId) {
        return celebrityMapper.selectList(new LambdaQueryWrapper<Celebrity>().eq(Celebrity::getFamilyId, familyId));
    }

    @Override
    public List<Celebrity> findByDeleted(Integer deleted) {
        return celebrityMapper.selectList(new LambdaQueryWrapper<Celebrity>().eq(Celebrity::getDeleted, deleted));
    }
}
