package com.clanhistory.controller.pub;

import com.clanhistory.entity.Celebrity;
import com.clanhistory.service.CelebrityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/celebrity")
@RequiredArgsConstructor
public class PublicCelebrityController {

    private final CelebrityService celebrityService;

    @GetMapping
    public ResponseEntity<List<Celebrity>> list() {
        return ResponseEntity.ok(celebrityService.findByDeleted(0));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Celebrity> getById(@PathVariable Long id) {
        Celebrity celebrity = celebrityService.findById(id);
        if (celebrity != null && celebrity.getDeleted() == 0) {
            return ResponseEntity.ok(celebrity);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<Celebrity>> getByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(celebrityService.findByFamilyId(familyId));
    }
}
