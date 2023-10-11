package gr.aueb.cf.springschoolapp.rest;

import gr.aueb.cf.springschoolapp.dto.teacher.TeacherInsertDTO;
import gr.aueb.cf.springschoolapp.dto.teacher.TeacherReadOnlyDTO;
import gr.aueb.cf.springschoolapp.dto.teacher.TeacherUpdateDTO;
import gr.aueb.cf.springschoolapp.model.Teacher;
import gr.aueb.cf.springschoolapp.service.ITeacherService;
import gr.aueb.cf.springschoolapp.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.springschoolapp.validator.TeacherInsertValidator;
import gr.aueb.cf.springschoolapp.validator.TeacherUpdateValidator;
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
public class TeacherRestController {

    private final ITeacherService teacherService;
    private final TeacherInsertValidator teacherInsertValidator;
    private final TeacherUpdateValidator teacherUpdateValidator;

    @Autowired
    public TeacherRestController(ITeacherService teacherService,
                                 TeacherInsertValidator teacherInsertValidator,
                                 TeacherUpdateValidator teacherUpdateValidator) {
        this.teacherService = teacherService;
        this.teacherInsertValidator = teacherInsertValidator;
        this.teacherUpdateValidator = teacherUpdateValidator;
    }

    @RequestMapping(path = "/teachers", method = RequestMethod.GET)
    public ResponseEntity<List<TeacherReadOnlyDTO>> getTeachersByLastname(@RequestParam("lastname") String lastname) {
        List<Teacher> teachers;
        try {
            teachers = teacherService.getTeachersByLastname(lastname);
            List<TeacherReadOnlyDTO> teachersDTO = new ArrayList<>();
            for (Teacher teacher : teachers) {
                teachersDTO.add(convertToReadOnlyDto(teacher));
            }
            return new ResponseEntity<>(teachersDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/teachers/{teacherId}", method = RequestMethod.GET)
    public ResponseEntity<TeacherReadOnlyDTO> getTeacher(@PathVariable("teacherId") Long teacherId) {
        Teacher teacher;
        try {
            teacher = teacherService.getTeacherById(teacherId);
            TeacherReadOnlyDTO teacherDTO = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/teachers", method = RequestMethod.POST)
    public ResponseEntity<TeacherReadOnlyDTO> addTeacher(@RequestBody TeacherInsertDTO dto, BindingResult bindingResult) {
        teacherInsertValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.insertTeacher(dto);
            TeacherReadOnlyDTO teacherDTO = convertToReadOnlyDto(teacher);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(teacherDTO.getId())
                    .toUri();

            return ResponseEntity.created(location).body(teacherDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/teachers/{teacherId}", method = RequestMethod.DELETE)
    public ResponseEntity<TeacherReadOnlyDTO> deleteTeacher(@PathVariable("teacherId") Long teacherId) {
        try {
            Teacher teacher = teacherService.getTeacherById(teacherId);
            teacherService.deleteTeacher(teacherId);
            TeacherReadOnlyDTO teacherDTO = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value= "teachers/{teacherId}", method = RequestMethod.PUT)
    public ResponseEntity<TeacherReadOnlyDTO> updateTeacher(@PathVariable("teacherId") Long teacherId,
                                                            @RequestBody TeacherUpdateDTO dto, BindingResult bindingResult) {

        if (!Objects.equals(teacherId, dto.getId())) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        teacherUpdateValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Teacher teacher = teacherService.updateTeacher(dto);
            TeacherReadOnlyDTO teacherDTO = convertToReadOnlyDto(teacher);
            return new ResponseEntity<>(teacherDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private TeacherReadOnlyDTO convertToReadOnlyDto(Teacher teacher) {
        TeacherReadOnlyDTO readOnlyDTO = new TeacherReadOnlyDTO();
        readOnlyDTO.setId(teacher.getId());
        readOnlyDTO.setFirstname(teacher.getFirstname());
        readOnlyDTO.setLastname(teacher.getLastname());
        return readOnlyDTO;
    }


}
