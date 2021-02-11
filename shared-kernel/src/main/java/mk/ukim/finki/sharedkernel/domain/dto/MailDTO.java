package mk.ukim.finki.sharedkernel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class MailDTO implements Serializable {

    @NotNull
    private String to;

    @NotNull
    private String subject;

    @NotNull
    private String text;
}
