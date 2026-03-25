package com.clanhistory.service;

import com.clanhistory.dto.ApprovalRequestDTO;
import com.clanhistory.entity.ApprovalRequest;
import java.util.List;

public interface ApprovalService {
    List<ApprovalRequest> findAll();
    ApprovalRequest findById(Long id);
    boolean create(ApprovalRequestDTO dto, Long userId);
    boolean approve(Long id, Long approverId, String comment);
    boolean reject(Long id, Long approverId, String comment);
    List<ApprovalRequest> findByStatus(String status);
    List<ApprovalRequest> findByRequestUserId(Long userId);
}
