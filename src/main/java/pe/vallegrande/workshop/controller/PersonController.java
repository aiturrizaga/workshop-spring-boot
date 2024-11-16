package pe.vallegrande.workshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.vallegrande.workshop.model.Person;
import pe.vallegrande.workshop.service.PersonService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/persons")
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        return ResponseEntity.ok(personService.save(person));
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons(@RequestParam(required = false) String type) {
        if (type != null) {
            return ResponseEntity.ok(personService.findByType(type));
        }
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Person>> getPersonById(@PathVariable String id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable String id, @RequestBody Person person) {
        return ResponseEntity.ok(personService.update(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
