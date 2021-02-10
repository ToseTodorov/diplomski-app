package mk.ukim.finki.diplomski.aplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiplomskaFullDTO implements Serializable {

    @NotNull
    private String id;

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
    private String submissionDate;

    @NotNull
    private String status;

    @NotNull
    private int statusNumber;

    private String file;

    @NotNull
    private String description;

    @NotNull
    private String scope;

    private String firstMemberNote;

    private String secondMemberNote;

    private String time; // vreme na odbrana

    private String date; // datum na odbrana

    private String location; // lokacija

    private String grade;

}
