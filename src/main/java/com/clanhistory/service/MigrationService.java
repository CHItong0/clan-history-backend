package com.clanhistory.service;

import com.clanhistory.entity.MigrationRoute;
import com.clanhistory.entity.MigrationPoint;
import java.util.List;

public interface MigrationService {
    // MigrationRoute operations
    List<MigrationRoute> findAllRoutes();
    MigrationRoute findRouteById(Long id);
    boolean saveRoute(MigrationRoute route);
    boolean updateRoute(MigrationRoute route);
    boolean deleteRoute(Long id);
    List<MigrationRoute> findRoutesByIsActive(Integer isActive);

    // MigrationPoint operations
    List<MigrationPoint> findPointsByRouteId(Long routeId);
    MigrationPoint findPointById(Long id);
    boolean savePoint(MigrationPoint point);
    boolean updatePoint(MigrationPoint point);
    boolean deletePoint(Long id);
}
