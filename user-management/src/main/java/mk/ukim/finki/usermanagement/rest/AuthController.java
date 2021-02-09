package mk.ukim.finki.usermanagement.rest;

import mk.ukim.finki.usermanagement.aplication.AuthenticationService;
import mk.ukim.finki.usermanagement.aplication.dto.LoginDTO;
import mk.ukim.finki.usermanagement.aplication.form.LoginForm;
import mk.ukim.finki.usermanagement.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@NotNull LoginForm loginForm){
        User user = authenticationService.login(loginForm);
        return ResponseEntity.ok(new LoginDTO(user.id().getId(), user.getRole().getRoleName()));
    }

}
