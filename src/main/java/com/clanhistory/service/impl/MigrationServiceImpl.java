package com.clanhistory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.dto.CoordinateDTO;
import com.clanhistory.entity.MigrationPoint;
import com.clanhistory.entity.MigrationRoute;
import com.clanhistory.mapper.MigrationPointMapper;
import com.clanhistory.mapper.MigrationRouteMapper;
import com.clanhistory.service.ChinaAreaService;
import com.clanhistory.service.MigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MigrationServiceImpl implements MigrationService {

    private final MigrationRouteMapper routeMapper;
    private final MigrationPointMapper pointMapper;
    private final ChinaAreaService chinaAreaService;

    @Override
    public List<MigrationRoute> findAllRoutes() {
        return routeMapper.selectList(null);
    }

    @Override
    public MigrationRoute findRouteById(Long id) {
        return routeMapper.selectById(id);
    }

    @Override
    public boolean saveRoute(MigrationRoute route) {
        return routeMapper.insert(route) > 0;
    }

    @Override
    public boolean updateRoute(MigrationRoute route) {
        return routeMapper.updateById(route) > 0;
    }

    @Override
    public boolean deleteRoute(Long id) {
        return routeMapper.deleteById(id) > 0;
    }

    @Override
    public List<MigrationRoute> findRoutesByIsActive(Integer isActive) {
        return routeMapper.selectList(new LambdaQueryWrapper<MigrationRoute>().eq(MigrationRoute::getIsActive, isActive));
    }

    @Override
    public List<MigrationPoint> findPointsByRouteId(Long routeId) {
        return pointMapper.selectList(new LambdaQueryWrapper<MigrationPoint>()
                .eq(MigrationPoint::getRouteId, routeId)
                .orderByAsc(MigrationPoint::getDisplayOrder));
    }

    @Override
    public MigrationPoint findPointById(Long id) {
        return pointMapper.selectById(id);
    }

    @Override
    public boolean savePoint(MigrationPoint point) {
        // Single-path enforcement
        if (point.getParentId() == null) {
            // Root point: ensure only one root per route
            Long existingRoot = pointMapper.selectCount(
                new LambdaQueryWrapper<MigrationPoint>()
                    .eq(MigrationPoint::getRouteId, point.getRouteId())
                    .isNull(MigrationPoint::getParentId)
            );
            if (existingRoot > 0) {
                throw new IllegalStateException("Route already has a root point. Only one root allowed per route.");
            }
        } else {
            // Non-root: check unique constraint (enforced by DB for non-NULL parent_id)
            Long siblingCount = pointMapper.selectCount(
                new LambdaQueryWrapper<MigrationPoint>()
                    .eq(MigrationPoint::getRouteId, point.getRouteId())
                    .eq(MigrationPoint::getParentId, point.getParentId())
            );
            if (siblingCount > 0) {
                throw new IllegalStateException("Parent already has a child point. Single-path only.");
            }
        }

        // Auto-fill coordinates from area names
        if (point.getProvince() != null || point.getCity() != null || point.getDistrict() != null) {
            CoordinateDTO coords = chinaAreaService.resolveCoordinate(
                point.getProvince(), point.getCity(), point.getDistrict());
            if (coords != null) {
                point.setLongitude(coords.getLng());
                point.setLatitude(coords.getLat());
            }
        }

        // Auto-generate point_name as "{province}-{city}-{district}" concatenation
        StringBuilder nameBuilder = new StringBuilder();
        if (point.getProvince() != null) nameBuilder.append(point.getProvince());
        if (point.getCity() != null) {
            if (nameBuilder.length() > 0) nameBuilder.append("-");
            nameBuilder.append(point.getCity());
        }
        if (point.getDistrict() != null) {
            if (nameBuilder.length() > 0) nameBuilder.append("-");
            nameBuilder.append(point.getDistrict());
        }
        if (nameBuilder.length() > 0) {
            point.setPointName(nameBuilder.toString());
        }

        // Auto-derive pointType from hierarchy depth
        if (point.getDistrict() != null && !point.getDistrict().isEmpty()) {
            point.setPointType("district");
        } else if (point.getCity() != null && !point.getCity().isEmpty()) {
            point.setPointType("city");
        } else if (point.getProvince() != null && !point.getProvince().isEmpty()) {
            point.setPointType("province");
        }

        return pointMapper.insert(point) > 0;
    }

    @Override
    public boolean updatePoint(MigrationPoint point) {
        // Single-path enforcement for non-root points
        if (point.getParentId() != null) {
            Long siblingCount = pointMapper.selectCount(
                new LambdaQueryWrapper<MigrationPoint>()
                    .eq(MigrationPoint::getRouteId, point.getRouteId())
                    .eq(MigrationPoint::getParentId, point.getParentId())
                    .ne(MigrationPoint::getId, point.getId())  // exclude self
            );
            if (siblingCount > 0) {
                throw new IllegalStateException("Parent already has a child point. Single-path only.");
            }
        }

        // Auto-fill coordinates from area names
        if (point.getProvince() != null || point.getCity() != null || point.getDistrict() != null) {
            CoordinateDTO coords = chinaAreaService.resolveCoordinate(
                point.getProvince(), point.getCity(), point.getDistrict());
            if (coords != null) {
                point.setLongitude(coords.getLng());
                point.setLatitude(coords.getLat());
            }
        }

        // Auto-generate point_name
        StringBuilder nameBuilder = new StringBuilder();
        if (point.getProvince() != null) nameBuilder.append(point.getProvince());
        if (point.getCity() != null) {
            if (nameBuilder.length() > 0) nameBuilder.append("-");
            nameBuilder.append(point.getCity());
        }
        if (point.getDistrict() != null) {
            if (nameBuilder.length() > 0) nameBuilder.append("-");
            nameBuilder.append(point.getDistrict());
        }
        if (nameBuilder.length() > 0) {
            point.setPointName(nameBuilder.toString());
        }

        // Auto-derive pointType
        if (point.getDistrict() != null && !point.getDistrict().isEmpty()) {
            point.setPointType("district");
        } else if (point.getCity() != null && !point.getCity().isEmpty()) {
            point.setPointType("city");
        } else if (point.getProvince() != null && !point.getProvince().isEmpty()) {
            point.setPointType("province");
        }

        return pointMapper.updateById(point) > 0;
    }

    @Override
    public boolean deletePoint(Long id) {
        return pointMapper.deleteById(id) > 0;
    }
}
