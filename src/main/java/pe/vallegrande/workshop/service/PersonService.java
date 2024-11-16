package pe.vallegrande.workshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.vallegrande.workshop.model.Person;
import pe.vallegrande.workshop.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public Person update(String id, Person person) {
        return personRepository.findById(id)
                .map(res -> {
                    res.setName(person.getName());
                    res.setLastname(person.getLastname());
                    res.setBirthday(person.getBirthday());
                    res.setAddress(person.getAddress());
                    return personRepository.save(res);
                }).orElseThrow();
    }

    public Optional<Person> findById(String id) {
        return personRepository.findById(id);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public List<Person> findByType(String type) {
        return personRepository.findByType(type.toUpperCase());
    }

    public void delete(String id) {
        personRepository.findById(id)
                .ifPresent(personRepository::delete);
    }
}
