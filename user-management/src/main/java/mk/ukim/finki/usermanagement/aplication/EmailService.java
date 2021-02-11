package mk.ukim.finki.usermanagement.aplication;


import mk.ukim.finki.usermanagement.domain.model.User;
import mk.ukim.finki.usermanagement.domain.repository.UserRepository;
import mk.ukim.finki.usermanagement.domain.value.UserId;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class EmailService {

    /*
        Using Gmail SMTP Server
        Need to set real username and password in application.properties for it to work
    */
    private final JavaMailSender emailSender;
    private final UserRepository userRepository;

    public EmailService(JavaMailSender emailSender, UserRepository userRepository) {
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        //message.setFrom("nosqlproject4@gmail.com"); //avtomatski e setirano od application.properties
        User user = userRepository.getOne(new UserId(UUID.fromString(to)));

        message.setTo(user.getEmail().getEmail()); // ne se validni mailovite
        //message.setTo("tose.todorov@hotmail.com");
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

}
