package mk.ukim.finki.diplomski.aplication.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class DiplomskaForm implements Serializable {

    @NotNull
    private String indeks;

    @NotNull
    private String title;

    @NotNull
    private String scope;

    @NotNull
    private String description;

    @NotNull
    private UUID mentor;

    @NotNull
    private UUID firstMember;

    @NotNull
    private UUID secondMember;

}
