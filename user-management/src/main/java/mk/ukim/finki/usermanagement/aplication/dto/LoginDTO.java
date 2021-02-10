package mk.ukim.finki.usermanagement.aplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mk.ukim.finki.sharedkernel.domain.role.RoleName;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginDTO {

    @NotNull
    private UUID userId;

    @NotNull
    private RoleName roleName;

    @NotNull
    private String username;

}
