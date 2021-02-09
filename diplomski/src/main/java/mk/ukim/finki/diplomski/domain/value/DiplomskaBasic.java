package mk.ukim.finki.diplomski.domain.value;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DiplomskaBasic {

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
}
