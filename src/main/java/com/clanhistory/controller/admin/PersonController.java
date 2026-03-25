package com.clanhistory.controller.admin;

import com.clanhistory.entity.Person;
import com.clanhistory.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<Person>> list() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Person person = personService.findById(id);
        if (person != null) {
            return ResponseEntity.ok(person);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/family/{familyId}")
    @PreAuthorize("hasAnyRole('super_admin', 'content_admin')")
    public ResponseEntity<List<Person>> getByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(personService.findByFamilyId(familyId));
    }

    @PostMapping
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> create(@RequestBody Person person) {
        if (personService.save(person)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Person person) {
        person.setId(id);
        if (personService.updateById(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('super_admin')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (personService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
