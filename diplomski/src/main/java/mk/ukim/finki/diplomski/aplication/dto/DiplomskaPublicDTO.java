package mk.ukim.finki.diplomski.aplication.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class DiplomskaPublicDTO implements Serializable {

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
    private String time;

    @NotNull
    private String date;
}
