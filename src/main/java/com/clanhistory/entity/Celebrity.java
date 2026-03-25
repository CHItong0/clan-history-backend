package com.clanhistory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("celebrity")
public class Celebrity {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("person_id")
    private Long personId;

    @TableField("family_id")
    private Long familyId;

    private String title;

    private String era;

    private String name;

    private String brief;

    private String biographyText;

    private String biography;

    private String achievements;

    private String honor;

    private String portrait;

    private Integer displayOrder;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
