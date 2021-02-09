package mk.ukim.finki.usermanagement.rest;


import mk.ukim.finki.sharedkernel.domain.user.Username;
import mk.ukim.finki.usermanagement.domain.exception.UserNotFoundException;
import mk.ukim.finki.usermanagement.domain.model.User;
import mk.ukim.finki.usermanagement.domain.repository.UserRepository;
import mk.ukim.finki.usermanagement.domain.value.RoleId;
import mk.ukim.finki.usermanagement.domain.value.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/role/{roleId}/ids")
    public List<UUID> findUserIdsByRole(@PathVariable("roleId") UUID roleId){
        List<User> usersWithRole = userRepository.findAllByRole_Id(new RoleId(roleId));
        return usersWithRole.stream()
                .map(user -> user.id().getId())
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") UUID userId) {
        return userRepository.findById(new UserId(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/username")
    public ResponseEntity<Username> findUsernameByUserId(@PathVariable("id") UUID userId) {
        return userRepository.findUsernameById(new UserId(userId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/role")
    public ResponseEntity<UUID> findRoleByUserOd(@PathVariable("id") UUID userId) {
        User user = userRepository.findById(new UserId(userId)).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(user.getRole().id().getId());
    }

    @GetMapping("/{username}/id")
    public ResponseEntity<UUID> findUserIdByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(userRepository.findIdByUsername(new Username(username)).getId());
    }

    @GetMapping("/{id}/fullname")
    public ResponseEntity<String> findFullnameByUserId(@PathVariable("id") UUID userId) {
        Optional<User> user = userRepository.findById(new UserId(userId));
        if (user.isPresent()){
            return ResponseEntity.ok(user.get().getFullName().toString());
        }

        return ResponseEntity.notFound().build();
    }

}
