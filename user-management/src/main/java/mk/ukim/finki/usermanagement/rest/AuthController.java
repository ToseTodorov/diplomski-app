package mk.ukim.finki.usermanagement.rest;

import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import mk.ukim.finki.usermanagement.aplication.AuthenticationService;
import mk.ukim.finki.usermanagement.aplication.dto.LoginDTO;
import mk.ukim.finki.usermanagement.aplication.form.LoginForm;
import mk.ukim.finki.usermanagement.aplication.form.UserForm;
import mk.ukim.finki.usermanagement.domain.model.Role;
import mk.ukim.finki.usermanagement.domain.model.User;
import mk.ukim.finki.usermanagement.domain.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;

    public AuthController(AuthenticationService authenticationService, RoleRepository roleRepository) {
        this.authenticationService = authenticationService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@NotNull @RequestBody LoginForm loginForm){
        User user = authenticationService.login(loginForm);
        return ResponseEntity.ok(new LoginDTO(user.id().getId(), user.getRole().getRoleName()));
    }

//    @GetMapping("/populate")
//    public ResponseEntity<String> populateDatabase(){
//
//        UserForm user1 = new UserForm();
//        user1.setFirstName("Дејан");
//        user1.setLastName("Сламков");
//        user1.setUsername("173036");
//        user1.setEmail("dejan.slamkov@mail.com");
//        user1.setPassword("Password123!");
//        Role role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Тоше");
//        user1.setLastName("Тодоров");
//        user1.setUsername("toshe.todorov");
//        user1.setEmail("toshe.todorov@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.PRODEKAN);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Венко");
//        user1.setLastName("Стојанов");
//        user1.setUsername("venko.stojanov");
//        user1.setEmail("venko.stojanov@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.PROFESSOR);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Драган");
//        user1.setLastName("Петков");
//        user1.setUsername("dragan.petkov");
//        user1.setEmail("dragan.petkov@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.ASSISTANT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Кирил");
//        user1.setLastName("Ристов");
//        user1.setUsername("kiril.ristov");
//        user1.setEmail("kiril.ristov@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.ST_SLUZBA);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Марија");
//        user1.setLastName("Танчева");
//        user1.setUsername("173014");
//        user1.setEmail("marija.tancheva@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        return ResponseEntity.ok("FALA BOGU!!!");
//    }

}
