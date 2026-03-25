package com.clanhistory.controller.admin;

import com.clanhistory.entity.Family;
import com.clanhistory.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<Family>> list() {
        return ResponseEntity.ok(familyService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<Family> getById(@PathVariable Long id) {
        Family family = familyService.findById(id);
        if (family != null) {
            return ResponseEntity.ok(family);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<Family>> getByStatus(@PathVariable Integer status) {
        // status: 1=active (deleted=0), 0=inactive (deleted=1)
        Integer deleted = (status == 1) ? 0 : 1;
        return ResponseEntity.ok(familyService.findByDeleted(deleted));
    }

    @PostMapping
    @PreAuthorize("hasRole('super_admin') or (hasRole('CONTENT_ADMIN') and @securityPermissionEvaluator.hasPermission(authentication, null, 'create:family'))")
    public ResponseEntity<Void> create(@RequestBody Family family, Authentication authentication) {
        if (familyService.save(family)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('super_admin') or (hasRole('CONTENT_ADMIN') and @securityPermissionEvaluator.hasPermission(authentication, null, 'update:family'))")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Family family) {
        family.setId(id);
        if (familyService.updateById(family)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (familyService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
