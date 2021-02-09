package mk.ukim.finki.usermanagement.domain.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
