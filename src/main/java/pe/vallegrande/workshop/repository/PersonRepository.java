package pe.vallegrande.workshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.vallegrande.workshop.model.Person;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, String> {
    List<Person> findByType(String type);
}
