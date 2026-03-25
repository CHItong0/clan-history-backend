package com.clanhistory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("family")
public class Family {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    @TableField("origin_place")
    private String originPlace;

    @TableField("cover_image")
    private String coverImage;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
