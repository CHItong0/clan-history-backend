package com.clanhistory.controller.admin;

import com.clanhistory.dto.ApprovalRequestDTO;
import com.clanhistory.entity.ApprovalRequest;
import com.clanhistory.entity.SysUser;
import com.clanhistory.service.ApprovalService;
import com.clanhistory.service.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;
    private final SysUserService userService;

    @GetMapping
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<List<ApprovalRequest>> list() {
        return ResponseEntity.ok(approvalService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<ApprovalRequest> getById(@PathVariable Long id) {
        ApprovalRequest request = approvalService.findById(id);
        if (request != null) {
            return ResponseEntity.ok(request);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<List<ApprovalRequest>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(approvalService.findByStatus(status));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<ApprovalRequest>> getMyRequests(Authentication authentication) {
        String username = authentication.getName();
        SysUser user = userService.findByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(approvalService.findByRequestUserId(user.getId()));
        }
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<Void> create(@Valid @RequestBody ApprovalRequestDTO dto, Authentication authentication) {
        String username = authentication.getName();
        SysUser user = userService.findByUsername(username);
        if (user != null && approvalService.create(dto, user.getId())) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> approve(@PathVariable Long id, @RequestParam(required = false) String comment,
                                        Authentication authentication) {
        String username = authentication.getName();
        SysUser user = userService.findByUsername(username);
        if (user != null && approvalService.approve(id, user.getId(), comment)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> reject(@PathVariable Long id, @RequestParam(required = false) String comment,
                                       Authentication authentication) {
        String username = authentication.getName();
        SysUser user = userService.findByUsername(username);
        if (user != null && approvalService.reject(id, user.getId(), comment)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
