package com.clanhistory.controller.admin;

import com.clanhistory.entity.SysUser;
import com.clanhistory.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService userService;

    @GetMapping
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<List<SysUser>> list() {
        return ResponseEntity.ok(userService.findAll().stream().peek(u -> u.setPassword(null)).toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<SysUser> getById(@PathVariable Long id) {
        SysUser user = userService.findById(id);
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> create(@RequestBody SysUser user) {
        if (userService.save(user)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        if (userService.updateById(user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
