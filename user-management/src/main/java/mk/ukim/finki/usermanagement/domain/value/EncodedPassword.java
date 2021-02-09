package mk.ukim.finki.usermanagement.domain.value;

import lombok.Getter;

import mk.ukim.finki.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Getter
@Embeddable
public class EncodedPassword implements ValueObject {

    @Transient
    private static final String PASSWORD_REGEX = "^[a-zA-Z@#$%^&+=](?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,32}$";

    @Column(name = "password", nullable = false)
    private final String password;

    public EncodedPassword(@NonNull String password) {
        this.password = password;
    }

    public static boolean validatePassword(String password){
        return password.matches(PASSWORD_REGEX);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncodedPassword encodedPassword1 = (EncodedPassword) o;
        return password.equals(encodedPassword1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
