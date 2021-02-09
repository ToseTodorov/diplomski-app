package mk.ukim.finki.diplomski.domain.value;


import mk.ukim.finki.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class UserId extends DomainObjectId {

    public UserId() {
        super();
    }

    public UserId(UUID id){
        super(id);
    }

}
