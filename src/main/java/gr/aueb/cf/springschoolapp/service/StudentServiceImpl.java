package gr.aueb.cf.springschoolapp.service;

import gr.aueb.cf.springschoolapp.dto.student.StudentInsertDTO;
import gr.aueb.cf.springschoolapp.dto.student.StudentUpdateDTO;
import gr.aueb.cf.springschoolapp.model.Student;
import gr.aueb.cf.springschoolapp.model.Teacher;
import gr.aueb.cf.springschoolapp.repository.StudentRepository;
import gr.aueb.cf.springschoolapp.service.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class StudentServiceImpl implements IStudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    @Override
    public Student insertStudent(StudentInsertDTO dto) throws Exception {
        Student student;
        try {
            student = studentRepository.save(convertInsertDtoToStudent(dto));
            if (student.getId() == null) {
                throw new Exception("Insert Exception");
            }
        } catch (Exception e) {
            log.info("Insert exception error");
            throw e;
        }
        return student;
    }

    @Transactional
    @Override
    public Student updateStudent(StudentUpdateDTO dto) throws EntityNotFoundException {
        Student student;
        Student updatedStudent;
        try {
            student = studentRepository.getById(dto.getId());
            if (student == null) throw new EntityNotFoundException(Teacher.class, dto.getId());
            updatedStudent = studentRepository.save(convertUpdateDtoToStudent(dto));
        } catch (EntityNotFoundException e) {
            log.info("Update exception error");
            throw e;
        }
        return updatedStudent;
    }

    @Transactional
    @Override
    public Student deleteStudent(Long id) throws EntityNotFoundException {
        Student student;

        try {
            student = studentRepository.findStudentById(id);
            if (student == null) throw new EntityNotFoundException(Teacher.class, id);
            studentRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            log.info("Delete exception error");
            throw e;
        }

        return student;
    }

    @Override
    public List<Student> getStudentsByLastname(String lastname) throws EntityNotFoundException {
        List<Student> students;
        try {
            students = studentRepository.findByLastnameStartingWith(lastname);
            if (students.size() == 0) throw new EntityNotFoundException(Teacher.class, 0L);
        } catch (EntityNotFoundException e) {
            log.info("Error in Get students by lastname");
            throw e;
        }

        return students;
    }

    @Override
    public Student getStudentById(Long id) throws EntityNotFoundException {
        Student student;
        try {
            student = studentRepository.findStudentById(id);
            if (student == null) throw new EntityNotFoundException(Teacher.class, id);
        } catch (EntityNotFoundException e) {
            log.info("Error in Get student by id");
            throw e;
        }

        return student;
    }

    private Student convertUpdateDtoToStudent(StudentUpdateDTO dto) {
        return new Student(dto.getId(), dto.getFirstname(), dto.getLastname());
    }

    private Student convertInsertDtoToStudent(StudentInsertDTO dto) {
        return new Student(null, dto.getFirstname(), dto.getLastname());
    }
}
