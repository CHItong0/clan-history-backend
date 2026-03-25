package com.clanhistory.controller.admin;

import com.clanhistory.dto.CityDTO;
import com.clanhistory.dto.DistrictDTO;
import com.clanhistory.dto.ProvinceDTO;
import com.clanhistory.entity.MigrationPoint;
import com.clanhistory.entity.MigrationRoute;
import com.clanhistory.service.ChinaAreaService;
import com.clanhistory.service.MigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/migration")
@RequiredArgsConstructor
public class MigrationController {

    private final MigrationService migrationService;
    private final ChinaAreaService chinaAreaService;

    // Route endpoints
    @GetMapping("/route")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<MigrationRoute>> listRoutes() {
        return ResponseEntity.ok(migrationService.findAllRoutes());
    }

    @GetMapping("/route/{id}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<MigrationRoute> getRouteById(@PathVariable Long id) {
        MigrationRoute route = migrationService.findRouteById(id);
        if (route != null) {
            return ResponseEntity.ok(route);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/route")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> createRoute(@RequestBody MigrationRoute route) {
        if (migrationService.saveRoute(route)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/route/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> updateRoute(@PathVariable Long id, @RequestBody MigrationRoute route) {
        route.setId(id);
        if (migrationService.updateRoute(route)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/route/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        if (migrationService.deleteRoute(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Point endpoints
    @GetMapping("/point/{routeId}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<MigrationPoint>> listPoints(@PathVariable Long routeId) {
        return ResponseEntity.ok(migrationService.findPointsByRouteId(routeId));
    }

    @GetMapping("/point/detail/{id}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<MigrationPoint> getPointById(@PathVariable Long id) {
        MigrationPoint point = migrationService.findPointById(id);
        if (point != null) {
            return ResponseEntity.ok(point);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/point")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> createPoint(@RequestBody MigrationPoint point) {
        if (migrationService.savePoint(point)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/point/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> updatePoint(@PathVariable Long id, @RequestBody MigrationPoint point) {
        point.setId(id);
        if (migrationService.updatePoint(point)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/point/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> deletePoint(@PathVariable Long id) {
        if (migrationService.deletePoint(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Area hierarchy endpoints (permit all for dropdowns)
    @GetMapping("/area/provinces")
    public ResponseEntity<List<ProvinceDTO>> getProvinces() {
        return ResponseEntity.ok(chinaAreaService.getAllProvinces());
    }

    @GetMapping("/area/cities/{provinceCode}")
    public ResponseEntity<List<CityDTO>> getCities(@PathVariable String provinceCode) {
        return ResponseEntity.ok(chinaAreaService.getCitiesByProvince(provinceCode));
    }

    @GetMapping("/area/districts/{cityCode}")
    public ResponseEntity<List<DistrictDTO>> getDistricts(@PathVariable String cityCode) {
        return ResponseEntity.ok(chinaAreaService.getDistrictsByCity(cityCode));
    }
}
