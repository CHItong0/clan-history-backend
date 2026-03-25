package com.clanhistory.dto;

import lombok.Data;
import java.util.List;

@Data
public class MigrationRouteDTO {
    private Long id;
    private String name;
    private Double originLng;
    private Double originLat;
    private Double destLng;
    private Double destLat;
    private List<PointDTO> points;

    @Data
    public static class PointDTO {
        private Long id;
        private Long parentId;
        private String name;        // pointName - for map labels
        private String province;
        private String city;       // NEW
        private String district;    // NEW
        private String pointType;   // province/city/district
        private Integer displayOrder;
        private Double longitude;
        private Double latitude;
        private String eventYear;
    }
}
