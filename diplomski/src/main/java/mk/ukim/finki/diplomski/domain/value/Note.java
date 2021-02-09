package mk.ukim.finki.diplomski.domain.value;

import lombok.Getter;
import mk.ukim.finki.sharedkernel.domain.base.ValueObject;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
@Getter
public class Note implements ValueObject {

    @Transient
    private static final int MAX_WORD_COUNT = 500;

    @Column(name = "note")
    private final String note;

    public Note() {
        this.note = "";
    }

    public Note(@NonNull String note) {
        note = note.trim();
        int wordCount = note.split("\\s+").length;
        if(wordCount > MAX_WORD_COUNT) {
            throw new RuntimeException(String.format("Note too long: %s!", note));
            // TODO: exception
        }
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note1 = (Note) o;
        return Objects.equals(note, note1.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(note);
    }
}
