package jmaster.io.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jmaster.io.demo.entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer> {

}
