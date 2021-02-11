package mk.ukim.finki.diplomski.aplication;

public interface EmailService {

    void sendMail(String to, String subject, String text);
}
