package com.internlink.internlink.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.internlink.internlink.model.FacultySupervisor;
import com.internlink.internlink.model.Student;

@Service
public class StudentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Student> getAllStudents() {
        return mongoTemplate.findAll(Student.class);
    }

    public Optional<Student> getStudentById(String studentId) {
        Query query = new Query(Criteria.where("studentId").is(studentId));
        Student student = mongoTemplate.findOne(query, Student.class);
        return Optional.ofNullable(student);
    }

    public Student register(Student student) {
        return mongoTemplate.save(student);
    }

    public Optional<Student> updateStudent(String studentId, Student updatedStudent) {
        return getStudentById(studentId).map(student -> {
            student.setName(updatedStudent.getName());
            student.setEmail(updatedStudent.getEmail());
            student.setMajor(updatedStudent.getMajor());
            student.setUniversity(updatedStudent.getUniversity());
            return mongoTemplate.save(student);
        });
    }

    public void deleteStudent(String studentId) {
        getStudentById(studentId).ifPresent(mongoTemplate::remove);
    }

    public String getStudentNameById(String studentId) {
        return getStudentById(studentId)
                .map(Student::getName)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));
    }

    public List<Student> getStudentsByFacultySupervisor(String facultySupervisorId) {
        // Step 1: Find the faculty supervisor document
        Query facultyQuery = new Query(Criteria.where("_id").is(facultySupervisorId));
        FacultySupervisor facultySupervisor = mongoTemplate.findOne(facultyQuery, FacultySupervisor.class);

        @SuppressWarnings("null")
        String supervisorId = facultySupervisor.getId(); // Assuming getter method exists

        // Step 3: Find students with matching supervisorId
        Query studentQuery = new Query(Criteria.where("supervisorId").is(supervisorId));
        return mongoTemplate.find(studentQuery, Student.class);
    }

    public List<Student> getStudentsByCompanySupervisor(String companySupervisorId) {
        Query query = new Query(Criteria.where("companySupervisorId").is(companySupervisorId));
        return mongoTemplate.find(query, Student.class);
    }
}
