package gr.aueb.cf.springschoolapp.service;

import gr.aueb.cf.springschoolapp.dto.teacher.TeacherInsertDTO;
import gr.aueb.cf.springschoolapp.dto.teacher.TeacherUpdateDTO;
import gr.aueb.cf.springschoolapp.model.Teacher;
import gr.aueb.cf.springschoolapp.repository.TeacherRepository;
import gr.aueb.cf.springschoolapp.service.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class TeacherServiceImpl implements ITeacherService{

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    @Override
    public Teacher insertTeacher(TeacherInsertDTO dto) throws Exception {
        Teacher teacher;
        try {
            teacher = teacherRepository.save(convertInsertDtoToTeacher(dto));
            if (teacher.getId() == null) {
                throw new Exception("Insert Exception");
            }
        } catch (Exception e) {
            log.info("Insert exception error");
            throw e;
        }

        return teacher;
    }

    @Transactional
    @Override
    public Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException {
        Teacher teacher;
        Teacher updatedTeacher;
        try {
            teacher = teacherRepository.getById(dto.getId());
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, dto.getId());
            updatedTeacher = teacherRepository.save(convertUpdateDtoToTeacher(dto));
        } catch (EntityNotFoundException e) {
            log.info("Update exception error");
            throw e;
        }

        return updatedTeacher;
    }

    @Transactional
    @Override
    public Teacher deleteTeacher(Long id) throws EntityNotFoundException {
        Teacher teacher;

        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, id);
            teacherRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            log.info("Delete Exception Error");
            throw e;
        }

        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByLastname(String lastname) throws EntityNotFoundException {
        List<Teacher> teachers;
        try {
            teachers = teacherRepository.findByLastnameStartingWith(lastname);
            if (teachers.size() == 0) throw new EntityNotFoundException(Teacher.class, 0L);
        } catch (EntityNotFoundException e) {
            log.info("Error in Get teachers by lastname");
            throw e;
        }

        return teachers;
    }

    @Override
    public Teacher getTeacherById(Long id) throws EntityNotFoundException {
        Teacher teacher;
        try {
            teacher = teacherRepository.findTeacherById(id);
            if (teacher == null) throw new EntityNotFoundException(Teacher.class, id);
        } catch (EntityNotFoundException e) {
            log.info("Error in Get teacher by id");
            throw e;
        }

        return teacher;
    }

    private Teacher convertUpdateDtoToTeacher(TeacherUpdateDTO dto) {
        return new Teacher(dto.getId(), dto.getFirstname(), dto.getLastname());
    }

    private Teacher convertInsertDtoToTeacher(TeacherInsertDTO dto) {
        return new Teacher(null, dto.getFirstname(), dto.getLastname());
    }
}
