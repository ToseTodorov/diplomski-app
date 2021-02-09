package mk.ukim.finki.diplomski.domain.value;

import lombok.Getter;
import mk.ukim.finki.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class FilePath implements ValueObject {

    @Column(name = "file_path")
    private final String filePath;

    public FilePath() {
        this.filePath = null;
    }

    public FilePath(@NonNull String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePath filePath1 = (FilePath) o;
        return Objects.equals(filePath, filePath1.filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }
}
