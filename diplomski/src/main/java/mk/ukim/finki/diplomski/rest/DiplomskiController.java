package mk.ukim.finki.diplomski.rest;

import mk.ukim.finki.diplomski.aplication.DiplomskiService;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaFullDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaPublicDTO;
import mk.ukim.finki.diplomski.aplication.form.DiplomskaForm;
import mk.ukim.finki.diplomski.aplication.form.OdbranaForm;
import mk.ukim.finki.sharedkernel.domain.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity submitNewDiplomska(@NotNull @RequestBody DiplomskaForm diplomskaForm,
                                             HttpServletRequest request){
        try {
            String userId = request.getHeader("user");
            diplomskiService.submitNewDiplomska(diplomskaForm, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Try again", e);
        }
    }

    @GetMapping("/professors")
    public ResponseEntity<List<UserDTO>> getAllProfessors(){
        return ResponseEntity.ok(diplomskiService.getAllProfessors());
    }

    @GetMapping("/teaching-staff")
    public ResponseEntity<List<UserDTO>> getAllProfessorsAndAssistants(){
        return ResponseEntity.ok(diplomskiService.getAllTeachingStaff());
    }

    @GetMapping("/diplomska/{id}")
    public ResponseEntity<DiplomskaFullDTO> getDiplomska(@PathVariable("id") String diplomskaId,
                                                         HttpServletRequest request){
        return ResponseEntity.ok(diplomskiService.getDiplomska(UUID.fromString(diplomskaId)));
    }

    @GetMapping("/student-diplomska")
    public ResponseEntity<DiplomskaFullDTO> getDiplomskaForStudent(HttpServletRequest request){
        String userId = request.getHeader("user");
        return ResponseEntity.ok(diplomskiService.getDiplomskaForStudent(UUID.fromString(userId)));
    }

    @PostMapping("/validate-cekor2")
    public ResponseEntity validateCekor2(@RequestParam String diplomskaId,
                                         HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.validateCekor2(UUID.fromString(diplomskaId), UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sluzba-validation-prijavuvanje")
    public ResponseEntity<List<DiplomskaFullDTO>> getDiplomskiForValidationBySluzba(HttpServletRequest request){
        String userId = request.getHeader("user");
        return ResponseEntity.ok(diplomskiService.getDiplomskiForValidationBySluzba(UUID.fromString(userId)));
    }

    @PostMapping("/validate-cekor3")
    public ResponseEntity validateCekor3(@RequestParam String diplomskaId,
                                         HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.validateCekor3(UUID.fromString(diplomskaId), UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/prodekan-diplomski")
    public ResponseEntity<List<DiplomskaFullDTO>> getDiplomskiForValidationByProdekan(HttpServletRequest request){
        String userId = request.getHeader("user");
        return ResponseEntity.ok(diplomskiService.getDiplomskiForValidationByProdekan(UUID.fromString(userId)));
    }

    @PostMapping("/validate-cekor3-1")
    public ResponseEntity validateCekor3_1(@RequestParam String diplomskaId,
                                         HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.validateCekor3_1(UUID.fromString(diplomskaId), UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mentor-diplomski")
    public ResponseEntity<List<DiplomskaFullDTO>> getDiplomskiForMentor(HttpServletRequest request){
        String userId = request.getHeader("user");
        return ResponseEntity.ok(diplomskiService.getDiplomskiForMentor(UUID.fromString(userId)));
    }

    @PostMapping(value = "/upload-file", consumes = {"multipart/form-data"}) // cekor 4
    public ResponseEntity uploadFile(@RequestParam String diplomskaId,
                                     @RequestParam MultipartFile file,
                                     HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.uploadFile(UUID.fromString(diplomskaId), UUID.fromString(userId), file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validate-cekor5")
    public ResponseEntity validateCekor5(@RequestParam String diplomskaId,
                                           HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.validateCekor5(UUID.fromString(diplomskaId), UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/submit-note")
    public ResponseEntity submitNote(@RequestParam String diplomskaId,
                                     @RequestParam String note,
                                     HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.uploadNote(UUID.fromString(diplomskaId), note, UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/komisija-diplomski")
    public ResponseEntity<List<DiplomskaFullDTO>> getDiplomskiForKomisija(HttpServletRequest request){
        String userId = request.getHeader("user");
        return ResponseEntity.ok(diplomskiService.getDiplomskiForKomisija(UUID.fromString(userId)));
    }

    @GetMapping("/sluzba-validation-odbrana")
    public ResponseEntity<List<DiplomskaFullDTO>> getDiplomskiForValidationBySluzbaOdbrana(HttpServletRequest request){
        String userId = request.getHeader("user");
        return ResponseEntity.ok(diplomskiService.getDiplomskiForValidationBySluzbaOdbrana(UUID.fromString(userId)));
    }

    @PostMapping("/validate-cekor6")
    public ResponseEntity validateCekor6(@RequestParam String diplomskaId,
                                         HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.validateCekor6(UUID.fromString(diplomskaId), UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }


    @PostMapping("/submit-odbrana")
    public ResponseEntity submitOdbranaInfo(@NotNull @RequestBody OdbranaForm odbranaForm,
                                            HttpServletRequest request) {
        String userId = request.getHeader("user");
        diplomskiService.updateOdbranaInfo(odbranaForm, UUID.fromString(userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/mail")
    public void mail(){
        diplomskiService.sendMail("daa2982a-90b6-43f0-b898-681047aa985a", "By Id", "test");
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
