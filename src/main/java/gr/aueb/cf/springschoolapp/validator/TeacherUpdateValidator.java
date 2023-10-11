package gr.aueb.cf.springschoolapp.validator;

import gr.aueb.cf.springschoolapp.dto.teacher.TeacherUpdateDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TeacherUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return TeacherUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        TeacherUpdateDTO teacherUpdateDTO = (TeacherUpdateDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname", "empty");
        if (teacherUpdateDTO.getFirstname().length() < 3 || teacherUpdateDTO.getLastname().length() > 50) {
            errors.rejectValue("firstname", "size");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastname", "empty");
        if (teacherUpdateDTO.getLastname().length() < 3 || teacherUpdateDTO.getLastname().length() > 50) {
            errors.rejectValue("lastname", "size");
        }
    }
}
