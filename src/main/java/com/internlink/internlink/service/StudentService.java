package com.internlink.internlink.service;

import java.util.List;

import org.bson.types.ObjectId;
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

    public Student getStudentById(String studentId) {
        return mongoTemplate.findOne(new Query(Criteria.where("studentId").is(studentId)), Student.class);
    }

    public Student register(Student student) {
        return mongoTemplate.save(student);
    }

    public Student updateStudent(String studentId, Student updatedStudent) {
        Student student = getStudentById(studentId);
        if (student == null) {
            return null;
        }
        student.setName(updatedStudent.getName());
        student.setEmail(updatedStudent.getEmail());
        student.setMajor(updatedStudent.getMajor());
        student.setUniversity(updatedStudent.getUniversity());
        return mongoTemplate.save(student);
    }

    public void deleteStudent(String studentId) {
        Student student = getStudentById(studentId);
        if (student != null) {
            mongoTemplate.remove(student);
        }
    }

    public List<Student> getStudentsByFacultySupervisor(String facultySupervisorId) {
        FacultySupervisor facultySupervisor = mongoTemplate.findOne(
                new Query(Criteria.where("_id").is(new ObjectId(facultySupervisorId))), FacultySupervisor.class);

        if (facultySupervisor == null) {
            throw new RuntimeException("Faculty Supervisor not found");
        }

        return mongoTemplate.find(new Query(Criteria.where("supervisorId").is(facultySupervisor.getSupervisorId())),
                Student.class);
    }

    public List<Student> getStudentsByCompanySupervisor(String companySupervisorId) {
        return mongoTemplate.find(new Query(Criteria.where("companySupervisorId").is(companySupervisorId)),
                Student.class);
    }
}
