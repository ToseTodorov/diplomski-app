package mk.ukim.finki.sharedkernel.domain.exception;

public class UsernameNotValidException extends RuntimeException {

    public UsernameNotValidException() {
    }

    public UsernameNotValidException(String message) {
        super(message);
    }

}
