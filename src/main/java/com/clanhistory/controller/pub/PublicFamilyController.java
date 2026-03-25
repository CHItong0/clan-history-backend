package com.clanhistory.controller.pub;

import com.clanhistory.entity.Family;
import com.clanhistory.service.FamilyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/family")
@RequiredArgsConstructor
public class PublicFamilyController {

    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity<List<Family>> list() {
        return ResponseEntity.ok(familyService.findByDeleted(0));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Family> getById(@PathVariable Long id) {
        Family family = familyService.findById(id);
        if (family != null && family.getDeleted() == 0) {
            return ResponseEntity.ok(family);
        }
        return ResponseEntity.notFound().build();
    }
}
