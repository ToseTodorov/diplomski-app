package mk.ukim.finki.diplomski.domain.value;

import lombok.Getter;
import mk.ukim.finki.sharedkernel.domain.base.ValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Embeddable
@Getter
public class Status implements ValueObject {

    @Column(name = "start_date", nullable = false)
    private final LocalDate startDate;

    @Column(name = "status", nullable = false)
    private final int status;

    @Transient
    private final String statusDescription;

    private static Map<Integer, String> statusMap = new HashMap<>(){
        {
            put(0, "Пријава");
            put(1, "Прифаќање на темата од студентот");
            put(2, "Валидирање од службата за студентски прашања");
            put(3, "Одобрение од продекан за настава");
            put(4, "Одобрение за оценка од ментор");
            put(5, "Забелешки од комисија");
            put(6, "Валидирање на услови за одбрана");
            put(7, "Одбрана");
            put(8, "Архива");
        }
    };

    public Status() {
        this.startDate = null;
        this.status = -1;
        this.statusDescription = "";
    }

    public Status(LocalDate startDate, int status) {
        this.startDate = startDate;
        this.status = status;
        this.statusDescription = statusMap.get(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status1 = (Status) o;
        return status == status1.status &&
                Objects.equals(startDate, status1.startDate) &&
                Objects.equals(statusDescription, status1.statusDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, status, statusDescription);
    }
}
