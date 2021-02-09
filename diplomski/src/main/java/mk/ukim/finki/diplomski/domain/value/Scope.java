package mk.ukim.finki.diplomski.domain.value;

import lombok.Getter;
import mk.ukim.finki.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Getter
@Embeddable
public class Scope implements ValueObject {

    @Transient
    private static final int MAX_CHARACTER_COUNT = 280;

    @Column(name = "scope", nullable = false)
    private final String scope;

    public Scope() {
        this.scope = null;
    }

    public Scope(@NonNull String scope){
        scope = scope.trim();
        if(scope.length() > MAX_CHARACTER_COUNT){
            throw new RuntimeException(String.format("Scope too long: %s!", scope));
            // TODO: exception
        }
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope1 = (Scope) o;
        return Objects.equals(scope, scope1.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope);
    }
}
