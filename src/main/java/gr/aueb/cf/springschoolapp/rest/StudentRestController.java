package gr.aueb.cf.springschoolapp.rest;

import gr.aueb.cf.springschoolapp.dto.student.StudentInsertDTO;
import gr.aueb.cf.springschoolapp.dto.student.StudentReadOnlyDTO;
import gr.aueb.cf.springschoolapp.dto.student.StudentUpdateDTO;
import gr.aueb.cf.springschoolapp.model.Student;
import gr.aueb.cf.springschoolapp.service.IStudentService;
import gr.aueb.cf.springschoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.springschoolapp.validator.StudentInsertValidator;
import gr.aueb.cf.springschoolapp.validator.StudentUpdateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private final IStudentService studentService;
    private final StudentInsertValidator studentInsertValidator;
    private final StudentUpdateValidator studentUpdateValidator;

    @Autowired
    public StudentRestController(IStudentService studentService,
                                 StudentInsertValidator studentInsertValidator,
                                 StudentUpdateValidator studentUpdateValidator) {
        this.studentService = studentService;
        this.studentInsertValidator = studentInsertValidator;
        this.studentUpdateValidator = studentUpdateValidator;
    }

    @RequestMapping(path= "/students", method = RequestMethod.GET)
    public ResponseEntity<List<StudentReadOnlyDTO>> getStudentsByLastname(@RequestParam("lastname") String lastname) {
        List<Student> students;
        try {
            students = studentService.getStudentsByLastname(lastname);
            List<StudentReadOnlyDTO> studentsDTO = new ArrayList<>();
            for (Student student : students) {
                studentsDTO.add(convertToReadOnlyDto(student));
            }
            return new ResponseEntity<>(studentsDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/students/{studentId}", method = RequestMethod.GET)
    public ResponseEntity<StudentReadOnlyDTO> getStudent(@PathVariable("studentId") Long studentId) {
        Student student;
        try {
            student = studentService.getStudentById(studentId);
            StudentReadOnlyDTO studentDTO = convertToReadOnlyDto(student);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/students", method = RequestMethod.POST)
    public ResponseEntity<StudentReadOnlyDTO> addStudent(@RequestBody StudentInsertDTO dto, BindingResult bindingResult) {
        studentInsertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Student student = studentService.insertStudent(dto);
            StudentReadOnlyDTO studentDTO = convertToReadOnlyDto(student);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(studentDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(studentDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/students/{studentId}", method = RequestMethod.DELETE)
    public ResponseEntity<StudentReadOnlyDTO> deleteStudent(@PathVariable("studentId") Long studentId) {
        try {
            Student student = studentService.getStudentById(studentId);
            studentService.deleteStudent(studentId);
            StudentReadOnlyDTO studentDTO = convertToReadOnlyDto(student);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/students/{studentId}", method = RequestMethod.PUT)
    public ResponseEntity<StudentReadOnlyDTO> updateTeacher(@PathVariable("studentId") Long studentId,
                                                            @RequestBody StudentUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(studentId, dto.getId())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        studentUpdateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Student student = studentService.updateStudent(dto);
            StudentReadOnlyDTO studentDTO = convertToReadOnlyDto(student);
            return new ResponseEntity<>(studentDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private StudentReadOnlyDTO convertToReadOnlyDto(Student student) {
        StudentReadOnlyDTO readOnlyDTO = new StudentReadOnlyDTO();
        readOnlyDTO.setId(student.getId());
        readOnlyDTO.setFirstname(student.getFirstname());
        readOnlyDTO.setLastname(student.getLastname());
        return readOnlyDTO;
    }
}
