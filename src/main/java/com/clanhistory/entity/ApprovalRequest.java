package com.clanhistory.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("approval_request")
public class ApprovalRequest {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("request_type")
    private String requestType;

    @TableField("entity_type")
    private String entityType;

    @TableField("entity_id")
    private Long entityId;

    @TableField("family_id")
    private Long familyId;

    @TableField("applicant_id")
    private Long applicantId;

    @TableField("original_data")
    private String originalData;

    @TableField("new_data")
    private String newData;

    private String status;

    @TableField("reviewer_id")
    private Long reviewerId;

    @TableField("review_comment")
    private String reviewComment;

    private LocalDateTime reviewedAt;

    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
