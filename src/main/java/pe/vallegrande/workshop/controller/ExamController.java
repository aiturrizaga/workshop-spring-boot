package pe.vallegrande.workshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.vallegrande.workshop.model.Exam;
import pe.vallegrande.workshop.model.ExamDetail;
import pe.vallegrande.workshop.service.ExamService;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/exams")
public class ExamController {

    private final ExamService examService;

    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams(@RequestParam(required = false) Boolean active) {
        if (active != null) {
            return ResponseEntity.ok(examService.findAll(active));
        }
        return ResponseEntity.ok(examService.findAll());
    }

    @PostMapping
    public ResponseEntity<Exam> saveExam(@RequestBody Exam exam) {
        return ResponseEntity.ok(examService.save(exam));
    }

    @PutMapping("/{examId}")
    public ResponseEntity<Exam> updateExam(@PathVariable UUID examId,
                                           @RequestBody Exam exam) {
        return ResponseEntity.ok(examService.update(examId, exam));
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> disableExam(@PathVariable UUID examId) {
        examService.disabledById(examId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{examId}/details")
    public ResponseEntity<Exam> addExamDetail(@PathVariable UUID examId,
                                              @RequestBody ExamDetail detail) {
        return ResponseEntity.ok(examService.addDetail(examId, detail));
    }
}
