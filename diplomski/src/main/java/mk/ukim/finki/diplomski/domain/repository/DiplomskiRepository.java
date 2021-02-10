package mk.ukim.finki.diplomski.domain.repository;

import mk.ukim.finki.diplomski.domain.model.Diplomska;
import mk.ukim.finki.diplomski.domain.value.DiplomskaId;
import mk.ukim.finki.diplomski.domain.value.Status;
import mk.ukim.finki.diplomski.domain.value.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiplomskiRepository extends JpaRepository<Diplomska, DiplomskaId> {

    Diplomska findDiplomskaByStudentId(UserId studentId);

    List<Diplomska> findAllByCurrentStatusEquals(Status status);

    List<Diplomska> findAllByMentorId(UserId mentorId);
}
