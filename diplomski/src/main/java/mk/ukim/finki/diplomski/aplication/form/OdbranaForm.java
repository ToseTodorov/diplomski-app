package mk.ukim.finki.diplomski.aplication.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class OdbranaForm implements Serializable {

    @NotNull
    private String diplomskaId;

    @NotNull
    private String date;

    @NotNull
    private String time;

    @NotNull
    private String location;
}
