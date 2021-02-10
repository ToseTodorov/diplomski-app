package mk.ukim.finki.diplomski.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.diplomski.domain.value.*;
import mk.ukim.finki.sharedkernel.domain.base.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Setter
@Table(name = "diplomski")
public class Diplomska extends AbstractEntity<DiplomskaId> {

    public Diplomska() {
        super(new DiplomskaId());
    }

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

    public Diplomska(UserId mentorId, UserId firstMemberId, UserId secondMemberId, UserId studentId,
                     Title title, Scope scope, Description description) {
        this.mentorId = mentorId;
        this.firstMemberId = firstMemberId;
        this.secondMemberId = secondMemberId;
        this.studentId = studentId;
        this.title = title;
        this.scope = scope;
        this.description = description;
        this.submissionDate = LocalDate.now();
        this.currentStatus = new Status(LocalDate.now(), 0);
    }

    public void updateStatus(){
        this.currentStatus = new Status(LocalDate.now(), this.currentStatus.getStatus()+1);
        // TODO: send email
    }

    public void updateFilePath(String filePath){
        this.filePath = new FilePath(filePath);
    }

    public void updateGrade(int grade){
        this.grade = new Grade(grade);
    }

    public void updateFirstMemberNote(String note){
        this.firstMemberNote = new Note(note);
    }

    public void updateSecondMemberNote(String note){
        this.secondMemberNote = new Note(note);
    }

    public void updateOdbranaInfo(LocalDateTime dateTime, String location){
        this.odbranaInfo = new Odbrana(dateTime, location);
    }

}
