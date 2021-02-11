package mk.ukim.finki.diplomski.domain.value;

import lombok.Getter;
import mk.ukim.finki.sharedkernel.domain.base.ValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
@Getter
public class Odbrana implements ValueObject {

    @Column(name = "date_time")
    private final LocalDateTime dateTime;

    @Column(name = "location")
    private final String location;

    public Odbrana() {
        this.dateTime = null;
        this.location = "";
    }

    public Odbrana(LocalDateTime dateTime, String location) {
//        if (dateTime.isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("Date should not be in the past");
//            // TODO: exception
//        }

        this.dateTime = dateTime;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Odbrana odbrana = (Odbrana) o;
        return Objects.equals(dateTime, odbrana.dateTime) &&
                Objects.equals(location, odbrana.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, location);
    }
}
