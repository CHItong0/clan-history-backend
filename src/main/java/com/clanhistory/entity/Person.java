package com.clanhistory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("person")
public class Person {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("family_id")
    private Long familyId;

    private Integer generation;

    private String name;

    private Integer gender;

    @TableField("birth_year")
    private String birthYear;

    @TableField("death_year")
    private String deathYear;

    @TableField("spouse_name")
    private String spouseName;

    @TableField("father_id")
    private Long fatherId;

    @TableField("bio_summary")
    private String bioSummary;

    @TableField("is_celebrity")
    private Integer isCelebrity;

    @TableField("display_order")
    private Integer displayOrder;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    // 不映射到数据库，仅用于前端树形结构
    @TableField(exist = false)
    private List<Person> children;
}
