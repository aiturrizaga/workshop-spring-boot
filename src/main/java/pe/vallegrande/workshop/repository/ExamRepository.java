package pe.vallegrande.workshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pe.vallegrande.workshop.model.Exam;

import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, String> {

    List<Exam> findByActive(boolean active);

    @Modifying
    @Query(value = "update Exam exam set exam.active = false where exam.id = ?1")
    void disableById(String id);
}
