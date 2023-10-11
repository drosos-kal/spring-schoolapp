package gr.aueb.cf.springschoolapp.service;

import gr.aueb.cf.springschoolapp.dto.teacher.TeacherInsertDTO;
import gr.aueb.cf.springschoolapp.dto.teacher.TeacherUpdateDTO;
import gr.aueb.cf.springschoolapp.model.Teacher;
import gr.aueb.cf.springschoolapp.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ITeacherService {
    Teacher insertTeacher(TeacherInsertDTO dto) throws Exception;
    Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException;
    Teacher deleteTeacher(Long id) throws EntityNotFoundException;
    List<Teacher> getTeachersByLastname(String lastname) throws EntityNotFoundException;
    Teacher getTeacherById(Long id) throws EntityNotFoundException;

    // List<Teacher> getAllTeachers TODO
}
