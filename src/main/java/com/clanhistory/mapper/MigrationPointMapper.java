package com.clanhistory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clanhistory.entity.MigrationPoint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MigrationPointMapper extends BaseMapper<MigrationPoint> {
}
