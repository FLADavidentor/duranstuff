package com.example.duranstuff.service;

import com.example.duranstuff.entity.Student;
import com.example.duranstuff.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        student.setId(id);
        return studentRepository.save(student);
    }

    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
