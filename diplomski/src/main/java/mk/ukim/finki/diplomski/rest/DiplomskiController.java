package mk.ukim.finki.diplomski.rest;

import mk.ukim.finki.diplomski.aplication.DiplomskiService;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaPublicDTO;
import mk.ukim.finki.diplomski.aplication.form.DiplomskaForm;
import mk.ukim.finki.diplomski.domain.model.Diplomska;
import mk.ukim.finki.diplomski.domain.value.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/diplomski")

public class DiplomskiController {

    private final DiplomskiService diplomskiService;

    public DiplomskiController(DiplomskiService diplomskiService) {
        this.diplomskiService = diplomskiService;
    }

    @GetMapping("/diploma-list")
    public ResponseEntity<List<DiplomskaDTO>> getAllDiplomski() {
        List<DiplomskaDTO> diplomski = diplomskiService.getAllDiplomski();
        return ResponseEntity.ok(diplomski);
    }

    @GetMapping("/public-dimploma-list")
    public ResponseEntity<List<DiplomskaPublicDTO>> getAllDiplomskiPublic() {
        List<DiplomskaPublicDTO> diplomski = diplomskiService.getAllDiplomskiPublic();
        return ResponseEntity.ok(diplomski);
    }

    @PostMapping("/submit")
    public ResponseEntity submitNewDiplomska(@NotNull DiplomskaForm diplomskaForm){
        try {
            diplomskiService.submitNewDiplomska(diplomskaForm);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Try again", e);
        }
    }


}
