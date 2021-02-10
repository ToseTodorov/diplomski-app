package mk.ukim.finki.usermanagement.domain.repository;



import mk.ukim.finki.sharedkernel.domain.base.DomainObjectId;
import mk.ukim.finki.sharedkernel.domain.user.Username;
import mk.ukim.finki.usermanagement.domain.model.User;
import mk.ukim.finki.usermanagement.domain.value.Email;
import mk.ukim.finki.usermanagement.domain.value.FullName;
import mk.ukim.finki.usermanagement.domain.value.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UserId> {

    Optional<User> findUserByUsername(Username username);

    Optional<User> findUserByEmail(Email email);

    List<User> findAllByRole_Id(DomainObjectId roleId);

    @Query("SELECT user.username FROM User user where user.id = :userId") // not working?
    Optional<Username> findUsernameById(@Param("userId") UserId userId);

    UserId findIdByUsername(Username username);
}

