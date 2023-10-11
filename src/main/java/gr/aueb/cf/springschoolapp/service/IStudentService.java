package gr.aueb.cf.springschoolapp.service;

import gr.aueb.cf.springschoolapp.dto.student.StudentInsertDTO;
import gr.aueb.cf.springschoolapp.dto.student.StudentUpdateDTO;
import gr.aueb.cf.springschoolapp.model.Student;
import gr.aueb.cf.springschoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface IStudentService {
    Student insertStudent(StudentInsertDTO dto) throws Exception;
    Student updateStudent(StudentUpdateDTO dto) throws EntityNotFoundException;
    Student deleteStudent(Long id) throws EntityNotFoundException;
    List<Student> getStudentsByLastname(String lastname) throws EntityNotFoundException;
    Student getStudentById(Long id) throws EntityNotFoundException;
}
