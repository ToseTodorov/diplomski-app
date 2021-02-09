package mk.ukim.finki.usermanagement.domain.exception;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException() {
    }

    public InvalidEmailException(String message) {
        super(message);
    }
}
