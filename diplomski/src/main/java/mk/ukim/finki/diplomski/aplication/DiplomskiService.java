package mk.ukim.finki.diplomski.aplication;

import mk.ukim.finki.diplomski.aplication.dto.DiplomskaDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaPublicDTO;
import mk.ukim.finki.diplomski.aplication.form.DiplomskaForm;
import mk.ukim.finki.diplomski.domain.model.Diplomska;
import mk.ukim.finki.diplomski.domain.repository.DiplomskiRepository;
import mk.ukim.finki.diplomski.domain.value.*;
import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DiplomskiService {
    private final DiplomskiRepository diplomskiRepository;
    private final UserService userService;
    private final RoleService roleService;

    public DiplomskiService(DiplomskiRepository diplomskiRepository, UserService userService, RoleService roleService) {
        this.diplomskiRepository = diplomskiRepository;
        this.userService = userService;
        this.roleService = roleService;
    }

    private boolean isInRole(@NonNull UserId userId, RoleName role){
        UUID roleId = userService.findRoleIdByUserId(userId.getId());
        RoleName roleName = roleService.findRoleNameByRoleId(roleId);
        return roleName.equals(role);
    }

    private boolean hasValidMembers(UserId mentorId, UserId firstMemberId, UserId secondMemberId, UserId studentId){
        if(!(isInRole(mentorId, RoleName.PROFESSOR) ||
            isInRole(mentorId, RoleName.PRODEKAN))) {
            return false;
        } else if (!(isInRole(firstMemberId, RoleName.PROFESSOR) ||
                    isInRole(firstMemberId, RoleName.PRODEKAN))) {
            return false;
        } else if (!(isInRole(secondMemberId, RoleName.PROFESSOR) ||
                    isInRole(secondMemberId, RoleName.ASSISTANT) ||
                    isInRole(secondMemberId, RoleName.PRODEKAN))) {
            return false;
        } else
            return isInRole(studentId, RoleName.STUDENT);
    }

    private String mapStudent(UUID studentId){
        String student = "";
        student += userService.findUsernameByUserId(studentId);
        student += " - " + userService.findFullNameByUserId(studentId);

        return student;
    }

    private DiplomskaBasic mapDiplomskaBasic(Diplomska diplomska){
        DiplomskaBasic tmp = new DiplomskaBasic();
        tmp.setStudent(mapStudent(diplomska.getStudentId().getId()));
        tmp.setMentor(userService.findFullNameByUserId(diplomska.getMentorId().getId()));
        tmp.setFirstMember(userService.findFullNameByUserId(diplomska.getFirstMemberId().getId()));
        tmp.setSecondMember(userService.findFullNameByUserId(diplomska.getSecondMemberId().getId()));
        tmp.setTitle(diplomska.getTitle().getTitle());

        return tmp;
    }


    private DiplomskaDTO mapDiplomskaDTO(Diplomska diplomska){
        DiplomskaBasic tmp = mapDiplomskaBasic(diplomska);
        DiplomskaDTO dto = new DiplomskaDTO();
        dto.setStudent(tmp.getStudent());
        dto.setMentor(tmp.getMentor());
        dto.setFirstMember(tmp.getFirstMember());
        dto.setSecondMember(tmp.getSecondMember());
        dto.setTitle(tmp.getTitle());
        dto.setDate(diplomska.getSubmissionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        dto.setStatus(diplomska.getCurrentStatus().getStatusDescription());
        dto.setFile(diplomska.getFilePath().getFilePath());
        dto.setDescription(diplomska.getDescription().getDescription());

        return dto;
    }

    private DiplomskaPublicDTO mapDiplomskaPublicDTO(Diplomska diplomska){
        DiplomskaBasic tmp = mapDiplomskaBasic(diplomska);
        DiplomskaPublicDTO dto = new DiplomskaPublicDTO();
        dto.setStudent(tmp.getStudent());
        dto.setMentor(tmp.getMentor());
        dto.setFirstMember(tmp.getFirstMember());
        dto.setSecondMember(tmp.getSecondMember());
        dto.setTitle(tmp.getTitle());
        dto.setTime(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        dto.setDate(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return dto;
    }

    public void submitNewDiplomska(DiplomskaForm diplomskaForm){
        UserId mentorId = new UserId(diplomskaForm.getMentor());
        UserId firstMemberId = new UserId(diplomskaForm.getFirstMember());
        UserId secondMemberId = new UserId(diplomskaForm.getSecondMember());
        UserId studentId = new UserId(userService.findUserIdByUsername(diplomskaForm.getIndeks()));

        if(!hasValidMembers(mentorId, firstMemberId, secondMemberId, studentId)) {
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        Title title = new Title(diplomskaForm.getTitle());
        Scope scope = new Scope(diplomskaForm.getScope());
        Description description = new Description(diplomskaForm.getDescription());
        Diplomska newDiplomska = new Diplomska(mentorId, firstMemberId, secondMemberId, studentId, title, scope, description);
        diplomskiRepository.saveAndFlush(newDiplomska);
    }


    public List<DiplomskaDTO> getAllDiplomski(){
        return diplomskiRepository.findAll()
                .stream()
                .map(this::mapDiplomskaDTO)
                .collect(Collectors.toList());
    }

    public List<DiplomskaPublicDTO> getAllDiplomskiPublic(){
        return diplomskiRepository.findAll()
                .stream()
                .map(this::mapDiplomskaPublicDTO)
                .collect(Collectors.toList());
    }


}
