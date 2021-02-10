package mk.ukim.finki.diplomski.rest;

import mk.ukim.finki.diplomski.aplication.DiplomskiService;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaPublicDTO;
import mk.ukim.finki.diplomski.aplication.form.DiplomskaForm;
import mk.ukim.finki.diplomski.domain.model.Diplomska;
import mk.ukim.finki.diplomski.domain.value.UserId;
import mk.ukim.finki.sharedkernel.domain.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/diplomski")
@CrossOrigin(origins = "*")
public class DiplomskiController {

    private final DiplomskiService diplomskiService;

    public DiplomskiController(DiplomskiService diplomskiService) {
        this.diplomskiService = diplomskiService;
    }

    @GetMapping("/diploma-list")
    public ResponseEntity<List<DiplomskaDTO>> getAllDiplomski(HttpServletRequest request) {
        List<DiplomskaDTO> diplomski = diplomskiService.getAllDiplomski();
        String userId = request.getHeader("user");
        String roleName = request.getHeader("role");
        return ResponseEntity.ok(diplomski);
    }

    @GetMapping("/public-diploma-list")
    public ResponseEntity<List<DiplomskaPublicDTO>> getAllDiplomskiPublic() {
        List<DiplomskaPublicDTO> diplomski = diplomskiService.getAllDiplomskiPublic();
        return ResponseEntity.ok(diplomski);
    }

    @PostMapping("/submit")
    public ResponseEntity submitNewDiplomska(@NotNull DiplomskaForm diplomskaForm,
                                             HttpServletRequest request){
        try {
            String userId = request.getHeader("user");
            diplomskiService.submitNewDiplomska(diplomskaForm, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Try again", e);
        }
    }

    @GetMapping("/professors")
    public ResponseEntity<List<UserDTO>> getAllProfessors(){
        return ResponseEntity.ok(diplomskiService.getAllProfessors());
    }

    @GetMapping("/teachingstaff")
    public ResponseEntity<List<UserDTO>> getAllProfessorsAndAssistants(){
        return ResponseEntity.ok(diplomskiService.getAllTeachingStaff());
    }

    @GetMapping("/populate")
    public ResponseEntity populate(){
        try {
            diplomskiService.populate();
            return ResponseEntity.ok().build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
