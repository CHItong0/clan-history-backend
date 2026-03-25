package com.clanhistory.controller.pub;

import com.clanhistory.entity.Person;
import com.clanhistory.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/genealogy")
@RequiredArgsConstructor
public class PublicGenealogyController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<Person>> list() {
        return ResponseEntity.ok(personService.buildGenealogyTree());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        Person person = personService.findById(id);
        if (person != null && person.getIsCelebrity() == 1) {
            return ResponseEntity.ok(person);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/family/{familyId}")
    public ResponseEntity<List<Person>> getByFamilyId(@PathVariable Long familyId) {
        return ResponseEntity.ok(personService.findByFamilyId(familyId));
    }
}
