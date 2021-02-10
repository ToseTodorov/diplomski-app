package mk.ukim.finki.usermanagement.rest;


import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import mk.ukim.finki.usermanagement.domain.exception.RoleNotFoundException;
import mk.ukim.finki.usermanagement.domain.model.Role;
import mk.ukim.finki.usermanagement.domain.repository.RoleRepository;
import mk.ukim.finki.usermanagement.domain.value.RoleId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/{roleName}/id")
    public UUID findRoleIdByRoleName(@PathVariable("roleName") RoleName roleName){
        Role role = roleRepository.findRoleByRoleName(roleName);
        return role.id().getId();
    }

    @GetMapping
    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findById(@PathVariable("id") UUID roleId) {
        return roleRepository.findById(new RoleId(roleId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<RoleName> isStudent(@PathVariable("id") UUID roleId){
        Role role = roleRepository.findById(new RoleId(roleId)).orElseThrow(RoleNotFoundException::new);
        return ResponseEntity.ok(role.getRoleName());
    }

//    @GetMapping("/populate")
//    public ResponseEntity<String> createRoles(){
//        Role assistant = new Role();
//        assistant.setRoleName(RoleName.ASSISTANT);
//        this.roleRepository.save(assistant);
//
//        Role prodekan = new Role();
//        prodekan.setRoleName(RoleName.PRODEKAN);
//        this.roleRepository.save(prodekan);
//
//        Role professor = new Role();
//        professor.setRoleName(RoleName.PROFESSOR);
//        this.roleRepository.save(professor);
//
//        Role stSluzba = new Role();
//        stSluzba.setRoleName(RoleName.ST_SLUZBA);
//        this.roleRepository.save(stSluzba);
//
//        Role student = new Role();
//        student.setRoleName(RoleName.STUDENT);
//        this.roleRepository.save(student);
//
//        this.roleRepository.flush();
//        return ResponseEntity.ok("YAYY!!");
//    }
}
