package gr.aueb.cf.springschoolapp.dto.student;

public class StudentInsertDTO {

    private String firstname;
    private String lastname;

    public StudentInsertDTO() {}
    public StudentInsertDTO(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
