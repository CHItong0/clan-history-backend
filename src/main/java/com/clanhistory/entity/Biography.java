package com.clanhistory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("biography")
public class Biography {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("celebrity_id")
    private Long celebrityId;

    @TableField("chapter_title")
    private String chapterTitle;

    @TableField("chapter_content")
    private String chapterContent;

    @TableField("chapter_order")
    private Integer chapterOrder;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
