package com.clanhistory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("migration_point")
public class MigrationPoint {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("route_id")
    private Long routeId;

    @TableField("parent_id")
    private Long parentId;  // Self-referential FK for hierarchical point structure

    @TableField("point_name")
    private String pointName;

    @TableField("point_type")
    private String pointType;

    private String province;

    private String city;    // City name

    private String district; // District name

    private Double longitude;

    private Double latitude;

    @TableField("event_year")
    private String eventYear;

    private Integer displayOrder;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
