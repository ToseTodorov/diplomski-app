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
public class Title implements ValueObject {

    @Transient
    private static final int MAX_CHARACTER_COUNT = 280;

    @Column(name = "title", nullable = false)
    private final String title;

    public Title() {
        this.title = null;
    }

    public Title(@NonNull String title){
        title = title.trim();
        if(title.length() > MAX_CHARACTER_COUNT){
            throw new RuntimeException(String.format("Title too long: %s!", title));
            // TODO: exception
        }
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Title)) return false;
        Title title1 = (Title) o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

}
