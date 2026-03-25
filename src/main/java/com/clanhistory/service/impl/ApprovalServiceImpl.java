package com.clanhistory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.clanhistory.dto.ApprovalRequestDTO;
import com.clanhistory.entity.ApprovalRequest;
import com.clanhistory.mapper.ApprovalRequestMapper;
import com.clanhistory.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRequestMapper approvalRequestMapper;

    @Override
    public List<ApprovalRequest> findAll() {
        return approvalRequestMapper.selectList(null);
    }

    @Override
    public ApprovalRequest findById(Long id) {
        return approvalRequestMapper.selectById(id);
    }

    @Override
    public boolean create(ApprovalRequestDTO dto, Long userId) {
        ApprovalRequest request = new ApprovalRequest();
        request.setRequestType(dto.getType());
        request.setEntityId(dto.getTargetId());
        request.setEntityType(dto.getTargetType());
        request.setStatus("pending");
        request.setApplicantId(userId);
        return approvalRequestMapper.insert(request) > 0;
    }

    @Override
    public boolean approve(Long id, Long approverId, String comment) {
        ApprovalRequest request = approvalRequestMapper.selectById(id);
        if (request != null && "pending".equals(request.getStatus())) {
            request.setStatus("approved");
            request.setReviewerId(approverId);
            request.setReviewComment(comment);
            request.setReviewedAt(java.time.LocalDateTime.now());
            return approvalRequestMapper.updateById(request) > 0;
        }
        return false;
    }

    @Override
    public boolean reject(Long id, Long approverId, String comment) {
        ApprovalRequest request = approvalRequestMapper.selectById(id);
        if (request != null && "pending".equals(request.getStatus())) {
            request.setStatus("rejected");
            request.setReviewerId(approverId);
            request.setReviewComment(comment);
            request.setReviewedAt(java.time.LocalDateTime.now());
            return approvalRequestMapper.updateById(request) > 0;
        }
        return false;
    }

    @Override
    public List<ApprovalRequest> findByStatus(String status) {
        return approvalRequestMapper.selectList(new LambdaQueryWrapper<ApprovalRequest>()
                .eq(ApprovalRequest::getStatus, status));
    }

    @Override
    public List<ApprovalRequest> findByRequestUserId(Long userId) {
        return approvalRequestMapper.selectList(new LambdaQueryWrapper<ApprovalRequest>()
                .eq(ApprovalRequest::getApplicantId, userId));
    }
}
