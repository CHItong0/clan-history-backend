package com.clanhistory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("historical_event")
public class HistoricalEvent {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("point_id")
    private Long pointId;

    @TableField("event_title")
    private String eventTitle;

    @TableField("event_content")
    private String eventContent;

    @TableField("event_year")
    private String eventYear;

    @TableField("event_month")
    private String eventMonth;

    private Integer displayOrder;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
