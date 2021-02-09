package mk.ukim.finki.diplomski.domain.exception;

public class UserNotAuthorizedException extends RuntimeException {

    public UserNotAuthorizedException() {
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
