package com.clanhistory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalRequestDTO {
    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Target ID is required")
    private Long targetId;

    @NotBlank(message = "Target type is required")
    private String targetType;

    @NotBlank(message = "Action is required")
    private String action;

    private String reason;
}
