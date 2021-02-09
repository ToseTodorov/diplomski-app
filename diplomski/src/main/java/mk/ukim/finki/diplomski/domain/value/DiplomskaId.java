package mk.ukim.finki.diplomski.domain.value;

import mk.ukim.finki.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class DiplomskaId extends DomainObjectId {

    public DiplomskaId() {
    }

    public DiplomskaId(UUID id) {
        super(id);
    }
}
