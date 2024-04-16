package com.papas.spring.security.controller;

import com.papas.spring.security.entity.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class StudentManagementController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "Fred Papa"),
            new Student(2, "Calvin Papa"),
            new Student(3, "Emmanuel Papa")
    );

    //permission based authentication ANNOTations:  hasRole("ROLE_") hasAnyRole("ROLE") hasAuthority("permission") hasAnyAuthority("permission")


    @GetMapping("/management")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public List<Student> getAllStudents(@RequestBody Student student){

        return STUDENTS;
    }

    @PostMapping("/management")
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student){

        System.out.println(student);
    }

    @PutMapping("/management/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")

    public void updateStudent(@PathVariable("studentId") Integer studentId,
                              @RequestBody Student student){
        System.out.println(String.format("%s %s", student, studentId));
    }

    @DeleteMapping("/management/{studentId}")
    @PreAuthorize("hasAuthority('student:write')")

    public void deleteStudent(@PathVariable("studentId") Integer studentId){

        System.out.println(studentId);
    }

}
