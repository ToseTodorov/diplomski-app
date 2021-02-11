package mk.ukim.finki.sharedkernel.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotNull
    private UUID id;

    @NotNull
    private String username;

    @NotNull
    private String fullname;

}
