package mk.ukim.finki.diplomski.domain.repository;

import mk.ukim.finki.diplomski.domain.model.Diplomska;
import mk.ukim.finki.diplomski.domain.value.DiplomskaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiplomskiRepository extends JpaRepository<Diplomska, DiplomskaId> {
}
