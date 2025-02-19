package com.internlink.internlink.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internlink.internlink.model.Student;
import com.internlink.internlink.service.StudentService;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    // @Autowired
    // private JwtUtil jwtUtil;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // @GetMapping
    // public ResponseEntity<List<Student>>
    // getStudentsBySupervisor(@RequestHeader("Authorization") String authHeader) {
    // String token = authHeader.substring(7);
    // Claims claims = jwtUtil.extractClaims(token);

    // String supervisorRole = claims.get("role", String.class);
    // String supervisorId = claims.getId();

    // if (!supervisorRole.equalsIgnoreCase("facultySupervisor")
    // && !supervisorRole.equalsIgnoreCase("companySupervisor")) {
    // throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid supervisor
    // role");
    // }

    // List<Student> students = supervisorRole.equalsIgnoreCase("facultySupervisor")
    // ? studentService.getStudentsByFacultySupervisor(supervisorId)
    // : studentService.getStudentsByCompanySupervisor(supervisorId);

    // return ResponseEntity.ok(students);
    // }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String studentId) {
        Optional<Student> student = studentService.getStudentById(studentId);
        return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setUserRole("STUDENT");
        mongoTemplate.save(student, "students");
        return ResponseEntity.ok("Student registered successfully!");
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable String studentId, @RequestBody Student updatedStudent) {
        Optional<Student> student = studentService.updateStudent(studentId, updatedStudent);
        return student.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}/name")
    public ResponseEntity<String> getStudentName(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentNameById(studentId));
    }
}
