package com.papas.spring.security.controller;

import com.papas.spring.security.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class StudentController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Fred Papa"),
            new Student(2, "Calvin Papa"),
            new Student(3, "Emmanuel Papa")
    );

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId){
        return  STUDENTS.stream()
                .filter(student -> studentId.equals(student.getStudentId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Student" + studentId + "does not exist"));
    }
}
