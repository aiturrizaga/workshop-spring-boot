package pe.vallegrande.workshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;
import org.hibernate.type.YesNoConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @Column(name = "id")
    private String id = UUID.randomUUID().toString();

    @Column(name = "teacher_id")
    private String teacherId;

    @Transient
    private String fullName;

    @Column(name = "topic")
    private String topic;

    @Column(name = "average")
    private BigDecimal average;

    @Column(name = "course")
    private String course;

    @Column(name = "active")
    @Convert(converter = NumericBooleanConverter.class)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "exam", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ExamDetail> details = new LinkedHashSet<>();

}
