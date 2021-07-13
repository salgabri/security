package com.security.security.student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Salvo"),
            new Student(2, "Claudio"),
            new Student(3, "Maria")
    );

    @GetMapping(path = "{id}")
    public Student getStudent(@PathVariable Integer id) {
        return STUDENTS.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
