package mk.ukim.finki.diplomski.domain.model;

import lombok.Getter;
import mk.ukim.finki.diplomski.domain.value.*;
import mk.ukim.finki.sharedkernel.domain.base.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "diplomski")
public class Diplomska extends AbstractEntity<DiplomskaId> {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "mentor_id", nullable = false))
    private UserId mentorId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "first_member_id", nullable = false))
    private UserId firstMemberId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "second_member_id", nullable = false))
    private UserId secondMemberId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "student_id", nullable = false))
    private UserId studentId;

    @Embedded
    private Title title;

    @Embedded
    private Description description;

    @Embedded
    private Scope scope;

    @Embedded
    private FilePath filePath;

    @Embedded
    private Grade grade;

    @Embedded
    @AttributeOverride(name = "note", column = @Column(name = "first_member_note"))
    private Note firstMemberNote;

    @Embedded
    @AttributeOverride(name = "note", column = @Column(name = "second_member_note"))
    private Note secondMemberNote;

    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @Embedded
    private Odbrana odbranaInfo;

    @Embedded
    private Status currentStatus;

}
