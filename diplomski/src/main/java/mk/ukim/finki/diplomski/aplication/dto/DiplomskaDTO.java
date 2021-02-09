package mk.ukim.finki.diplomski.aplication.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DiplomskaDTO implements Serializable {

    @NotNull
    private String title;

    @NotNull
    private String student;

    @NotNull
    private String mentor;

    @NotNull
    private String firstMember;

    @NotNull
    private String secondMember;

    @NotNull
    private String date;

    @NotNull
    private String status;

    @NotNull
    private String file;

    @NotNull
    private String description;
}
