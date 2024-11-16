package pe.vallegrande.workshop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.vallegrande.workshop.model.Exam;
import pe.vallegrande.workshop.model.ExamDetail;
import pe.vallegrande.workshop.model.Person;
import pe.vallegrande.workshop.repository.ExamRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final PersonService personService;

    public List<Exam> findAll() {
        List<Exam> exams = examRepository.findAll();
        return examPersonList(exams);
    }

    public List<Exam> findAll(boolean active) {
        List<Exam> exams = examRepository.findByActive(active);
        return examPersonList(exams);
    }

    @Transactional
    public void disabledById(UUID id) {
        examRepository.disableById(id.toString());
    }

    public Exam save(Exam exam) {
        if (exam.getDetails() != null && !exam.getDetails().isEmpty()) {
            exam.getDetails().forEach(detail -> detail.setExam(exam));
        }

        BigDecimal average = calculateAverage(exam.getDetails());
        exam.setAverage(average);

        Exam newExam = examRepository.save(exam);
        return examPerson(newExam);
    }

    public Exam update(UUID examId, Exam req) {
        return examRepository.findById(examId.toString())
                .map(exam -> {
                    exam.setTeacherId(req.getTeacherId());
                    exam.setTopic(req.getTopic());
                    exam.setCourse(req.getCourse());
                    exam.setUpdatedAt(LocalDateTime.now());
                    Exam updateExam = examRepository.save(exam);
                    return examPerson(updateExam);
                })
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with ID: " + examId));
    }

    public Exam addDetail(UUID examId, ExamDetail examDetail) {
        return examRepository.findById(examId.toString())
                .map(exam -> {
                    examDetail.setExam(exam);
                    exam.getDetails().add(examDetail);
                    exam.setAverage(calculateAverage(exam.getDetails()));
                    Exam updateExam = examRepository.save(exam);
                    return examPerson(updateExam);
                })
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with ID: " + examId));
    }

    private BigDecimal calculateAverage(Set<ExamDetail> details) {
        if (details == null || details.isEmpty()) {
            return BigDecimal.ZERO;
        }

        double average = details.stream()
                .map(ExamDetail::getScore)
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .orElse(0.0);

        return BigDecimal.valueOf(average);
    }

    private Exam examPerson(Exam examEntity) {
        personService.findById(examEntity.getTeacherId())
                .ifPresent(person -> examEntity.setFullName(getFullName(person)));
        examEntity.setDetails(examPersonDetailList(examEntity.getDetails()));
        return examEntity;
    }

    private List<Exam> examPersonList(List<Exam> examsEntity) {
        List<Exam> exams = new ArrayList<>();
        examsEntity.forEach(exam -> exams.add(examPerson(exam)));
        return exams;
    }

    private Set<ExamDetail> examPersonDetailList(Set<ExamDetail> examDetailsEntity) {
        Set<ExamDetail> examDetails = new LinkedHashSet<>();
        examDetailsEntity.forEach(exam -> {
            personService.findById(exam.getStudentId())
                    .ifPresent(person -> exam.setFullName(getFullName(person)));
            examDetails.add(exam);
        });
        return examDetails;
    }

    private String getFullName(Person person) {
        return person.getName() + " " + person.getLastname();
    }

}
