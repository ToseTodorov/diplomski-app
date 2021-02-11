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
        LoginDTO login = new LoginDTO(user.id().getId(), user.getRole().getRoleName(), user.getUsername().getUsername(), user.getFullName().toString());
        return ResponseEntity.ok(login);
    }

    @GetMapping("/populate")
    public ResponseEntity<String> populateDatabase(){

        UserForm user1 = new UserForm();
        user1.setFirstName("Атанас");
        user1.setLastName("Атанасов");
        user1.setUsername("171994");
        user1.setEmail("atanas.atanasov@mail.com");
        user1.setPassword("Password123!");
        Role role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
        user1.setRoleId(role.id().getId());
        this.authenticationService.register(user1);

        user1 = new UserForm();
        user1.setFirstName("Ангел");
        user1.setLastName("Ангелов");
        user1.setUsername("171993");
        user1.setEmail("angel.angelov@mail.com");
        user1.setPassword("Password123!");
        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
        user1.setRoleId(role.id().getId());
        this.authenticationService.register(user1);

        user1 = new UserForm();
        user1.setFirstName("Пеце");
        user1.setLastName("Пецев");
        user1.setUsername("171992");
        user1.setEmail("pece.pecev@mail.com");
        user1.setPassword("Password123!");
        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
        user1.setRoleId(role.id().getId());
        this.authenticationService.register(user1);

        user1 = new UserForm();
        user1.setFirstName("Валентин");
        user1.setLastName("Валентинов");
        user1.setUsername("171991");
        user1.setEmail("valentin.valentinov@mail.com");
        user1.setPassword("Password123!");
        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
        user1.setRoleId(role.id().getId());
        this.authenticationService.register(user1);

        user1 = new UserForm();
        user1.setFirstName("Стефан");
        user1.setLastName("Стефанов");
        user1.setUsername("171990");
        user1.setEmail("stefan.stefanov@mail.com");
        user1.setPassword("Password123!");
        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
        user1.setRoleId(role.id().getId());
        this.authenticationService.register(user1);

        return ResponseEntity.ok("FALA BOGU!!!");
    }

//    @GetMapping("/populate")
//    public ResponseEntity<String> populateDatabase(){
//
//        UserForm user1 = new UserForm();
//        user1.setFirstName("Петко");
//        user1.setLastName("Петков");
//        user1.setUsername("171999");
//        user1.setEmail("petko.petkov@mail.com");
//        user1.setPassword("Password123!");
//        Role role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        UserForm user2 = new UserForm();
//        user2.setFirstName("Трајко");
//        user2.setLastName("Трајков");
//        user2.setUsername("171998");
//        user2.setEmail("trajko.trajkov@mail.com");
//        user2.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
//        user2.setRoleId(role.id().getId());
//        this.authenticationService.register(user2);
//
//        UserForm user3 = new UserForm();
//        user3.setFirstName("Аце");
//        user3.setLastName("Ацев");
//        user3.setUsername("171997");
//        user3.setEmail("ace.acev@mail.com");
//        user3.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
//        user3.setRoleId(role.id().getId());
//        this.authenticationService.register(user3);
//
//        user1 = new UserForm();
//        user1.setFirstName("Алберт");
//        user1.setLastName("Ајнштајн");
//        user1.setUsername("171996");
//        user1.setEmail("albert.einstein@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Исак");
//        user1.setLastName("Њутн");
//        user1.setUsername("171995");
//        user1.setEmail("isaac.newton@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.STUDENT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Васе");
//        user1.setLastName("Васев");
//        user1.setUsername("vase.vasev");
//        user1.setEmail("vase.vasev@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.PRODEKAN);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Carl");
//        user1.setLastName("Sagan");
//        user1.setUsername("carl.sagan");
//        user1.setEmail("carl.sagan@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.PROFESSOR);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//
//        user1 = new UserForm();
//        user1.setFirstName("Stanley");
//        user1.setLastName("Kubrick");
//        user1.setUsername("stanley.kubrick");
//        user1.setEmail("stanley.kubrick@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.PROFESSOR);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Heywood");
//        user1.setLastName("Floyd");
//        user1.setUsername("heywood.floyd");
//        user1.setEmail("heywood.floyd@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.PROFESSOR);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("David");
//        user1.setLastName("Bowman");
//        user1.setUsername("david.bowman");
//        user1.setEmail("david.bowman@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.ASSISTANT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Гоце");
//        user1.setLastName("Гоцев");
//        user1.setUsername("goce.gocev");
//        user1.setEmail("goce.gocev@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.ASSISTANT);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        user1 = new UserForm();
//        user1.setFirstName("Кирил");
//        user1.setLastName("Наков");
//        user1.setUsername("kiril.nakov");
//        user1.setEmail("kiril.nakov@mail.com");
//        user1.setPassword("Password123!");
//        role = this.roleRepository.findRoleByRoleName(RoleName.ST_SLUZBA);
//        user1.setRoleId(role.id().getId());
//        this.authenticationService.register(user1);
//
//        return ResponseEntity.ok("FALA BOGU!!!");
//    }


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
