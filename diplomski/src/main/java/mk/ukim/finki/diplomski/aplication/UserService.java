package mk.ukim.finki.diplomski.aplication;

import mk.ukim.finki.diplomski.domain.value.UserId;
import mk.ukim.finki.sharedkernel.domain.user.Username;

import java.util.UUID;
import java.util.stream.Stream;

public interface UserService {

    Stream<UserId> findAllUserIdsByRoleId(UUID roleId);

    Username findUsernameByUserId(UUID userId);

    UUID findRoleIdByUserId(UUID userId);

    UUID findUserIdByUsername(String username);

    String findFullNameByUserId(UUID userId);

}
