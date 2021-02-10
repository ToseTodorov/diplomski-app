package mk.ukim.finki.diplomski.domain.value;

import lombok.Getter;
import lombok.NonNull;
import mk.ukim.finki.sharedkernel.domain.base.ValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Embeddable
@Getter
public class Grade implements ValueObject {

    @Column(name = "grade")
    private final int grade;

    @Transient
    private final String gradeDescription;

    private static final Map<Integer, String> gradeMap;
    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(5, "пет");
        map.put(6, "шест");
        map.put(7, "седум");
        map.put(8, "осум");
        map.put(9, "девет");
        map.put(10, "десет");
        gradeMap = Collections.unmodifiableMap(map);
    }


    public Grade(){
        this.grade = -1;
        gradeDescription = "";
    }

    public Grade(@NonNull int grade) {
        if(grade < 5 || grade > 10) {
            throw new RuntimeException("Grade is not valid");
            // TODO: exception
        }
        this.grade = grade;
        this.gradeDescription = gradeMap.get(grade);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grade grade1 = (Grade) o;
        return grade == grade1.grade &&
                Objects.equals(gradeDescription, grade1.gradeDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grade, gradeDescription);
    }

    @Override
    public String toString() {
        return this.grade + " (" + this.gradeDescription + ")";
    }
}
