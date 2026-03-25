package com.clanhistory.controller.admin;

import com.clanhistory.entity.Celebrity;
import com.clanhistory.service.CelebrityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/celebrity")
@RequiredArgsConstructor
public class CelebrityController {

    private final CelebrityService celebrityService;

    @GetMapping
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<Celebrity>> list() {
        return ResponseEntity.ok(celebrityService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<Celebrity> getById(@PathVariable Long id) {
        Celebrity celebrity = celebrityService.findById(id);
        if (celebrity != null) {
            return ResponseEntity.ok(celebrity);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/family/{familyId}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<Celebrity>> getByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(celebrityService.findByFamilyId(familyId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<Void> create(@RequestBody Celebrity celebrity) {
        if (celebrityService.save(celebrity)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Celebrity celebrity) {
        celebrity.setId(id);
        if (celebrityService.updateById(celebrity)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (celebrityService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
