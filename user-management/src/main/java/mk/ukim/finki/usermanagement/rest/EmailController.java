package mk.ukim.finki.usermanagement.rest;

import mk.ukim.finki.sharedkernel.domain.dto.MailDTO;
import mk.ukim.finki.usermanagement.aplication.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/mail")
@CrossOrigin(origins = "*")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity sendMail(@RequestBody MailDTO dto){
        emailService.sendSimpleEmail(dto.getTo(),dto.getSubject(),dto.getText());
        return ResponseEntity.ok().build();
    }
}
