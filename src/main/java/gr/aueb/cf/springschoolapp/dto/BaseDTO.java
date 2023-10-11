package gr.aueb.cf.springschoolapp.dto;

import com.sun.istack.NotNull;

public abstract class BaseDTO {
    @NotNull
    private long id;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
