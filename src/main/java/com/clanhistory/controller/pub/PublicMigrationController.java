package com.clanhistory.controller.pub;

import com.clanhistory.dto.MigrationRouteDTO;
import com.clanhistory.entity.MigrationPoint;
import com.clanhistory.entity.MigrationRoute;
import com.clanhistory.service.MigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/migration")
@RequiredArgsConstructor
public class PublicMigrationController {

    private final MigrationService migrationService;

    @GetMapping
    public ResponseEntity<List<MigrationRouteDTO>> listRoutes() {
        List<MigrationRoute> routes = migrationService.findRoutesByIsActive(1);
        List<MigrationRouteDTO> dtos = routes.stream().map(route -> {
            MigrationRouteDTO dto = new MigrationRouteDTO();
            dto.setId(route.getId());
            dto.setName(route.getRouteName());

            List<MigrationPoint> points = migrationService.findPointsByRouteId(route.getId());
            if (points != null && !points.isEmpty()) {
                // Build ordered list by walking parent chain from root point
                List<MigrationPoint> orderedPoints = buildOrderedPoints(points);

                MigrationPoint first = orderedPoints.get(0);
                MigrationPoint last = orderedPoints.get(orderedPoints.size() - 1);
                dto.setOriginLng(first.getLongitude());
                dto.setOriginLat(first.getLatitude());
                dto.setDestLng(last.getLongitude());
                dto.setDestLat(last.getLatitude());

                // Return points in hierarchy order (backward compatible)
                List<MigrationRouteDTO.PointDTO> pointDtos = orderedPoints.stream().map(p -> {
                    MigrationRouteDTO.PointDTO pd = new MigrationRouteDTO.PointDTO();
                    pd.setId(p.getId());
                    pd.setParentId(p.getParentId());
                    pd.setName(p.getPointName());
                    pd.setProvince(p.getProvince());
                    pd.setCity(p.getCity());
                    pd.setDistrict(p.getDistrict());
                    pd.setPointType(p.getPointType());
                    pd.setDisplayOrder(p.getDisplayOrder());
                    pd.setLongitude(p.getLongitude());
                    pd.setLatitude(p.getLatitude());
                    pd.setEventYear(p.getEventYear());
                    return pd;
                }).collect(Collectors.toList());
                dto.setPoints(pointDtos);
            }
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private List<MigrationPoint> buildOrderedPoints(List<MigrationPoint> points) {
        if (points == null || points.isEmpty()) {
            return Collections.emptyList();
        }

        // Build parent-child map
        Map<Long, MigrationPoint> pointMap = new HashMap<>();
        for (MigrationPoint p : points) {
            pointMap.put(p.getId(), p);
        }

        // Find root point (parentId == null)
        MigrationPoint root = null;
        for (MigrationPoint p : points) {
            if (p.getParentId() == null) {
                root = p;
                break;
            }
        }

        // If we have a valid parent chain with multiple points, walk it
        if (root != null) {
            List<MigrationPoint> ordered = new ArrayList<>();
            MigrationPoint current = root;
            while (current != null) {
                ordered.add(current);
                current = pointMap.get(current.getParentId());
                // Prevent infinite loop
                if (ordered.size() > points.size()) break;
            }

            // Add any remaining points not in the chain (orphans)
            Set<Long> inChain = new HashSet<>();
            for (MigrationPoint p : ordered) {
                inChain.add(p.getId());
            }
            for (MigrationPoint p : points) {
                if (!inChain.contains(p.getId())) {
                    ordered.add(p);
                }
            }

            // If we got all points from the chain, return it
            if (ordered.size() == points.size()) {
                return ordered;
            }
        }

        // Default: sort by displayOrder (handles flat data and orphan fallback)
        return points.stream()
                .sorted(Comparator.comparing(MigrationPoint::getDisplayOrder))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MigrationRoute> getRouteById(@PathVariable Long id) {
        MigrationRoute route = migrationService.findRouteById(id);
        if (route != null && route.getIsActive() == 1) {
            return ResponseEntity.ok(route);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<List<MigrationPoint>> getPoints(@PathVariable Long id) {
        return ResponseEntity.ok(migrationService.findPointsByRouteId(id));
    }
}
