package com.example.duranstuff.repository;

import com.example.duranstuff.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
