package mk.ukim.finki.diplomski.aplication;

import mk.ukim.finki.diplomski.aplication.dto.DiplomskaDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaPublicDTO;
import mk.ukim.finki.diplomski.aplication.form.DiplomskaForm;
import mk.ukim.finki.diplomski.domain.model.Diplomska;
import mk.ukim.finki.diplomski.domain.repository.DiplomskiRepository;
import mk.ukim.finki.diplomski.domain.value.*;
import mk.ukim.finki.sharedkernel.domain.dto.UserDTO;
import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        student += userService.findUsernameByUserId(studentId).getUsername();
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
        // TODO: no time; not date; handling
        dto.setTime(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        dto.setDate(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        return dto;
    }

    public void submitNewDiplomska(DiplomskaForm diplomskaForm, String userId){
        UUID id = UUID.fromString(userId);
        UUID roleId = userService.findRoleIdByUserId(id);
        RoleName roleName = roleService.findRoleNameByRoleId(roleId);
        if(!(roleName == RoleName.PROFESSOR || roleName == RoleName.PRODEKAN)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }
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


    public List<UserDTO> getAllProfessors() {
        UUID professorRoleId = roleService.findRoleIdByRoleName(RoleName.PROFESSOR);
        UUID prodekanRoleId = roleService.findRoleIdByRoleName(RoleName.PRODEKAN);

        Set<UserDTO> users = new HashSet<>(userService.findAllUsersByRoleId(professorRoleId));
        users.addAll(userService.findAllUsersByRoleId(prodekanRoleId));
        return List.copyOf(users);
    }

    public List<UserDTO> getAllTeachingStaff() {
        UUID professorRoleId = roleService.findRoleIdByRoleName(RoleName.PROFESSOR);
        UUID prodekanRoleId = roleService.findRoleIdByRoleName(RoleName.PRODEKAN);
        UUID assistantRoleId = roleService.findRoleIdByRoleName(RoleName.ASSISTANT);

        Set<UserDTO> users = new HashSet<>(userService.findAllUsersByRoleId(professorRoleId));
        users.addAll(userService.findAllUsersByRoleId(assistantRoleId));
        users.addAll(userService.findAllUsersByRoleId(prodekanRoleId));

        return List.copyOf(users);
    }

    public void populate() {
        Diplomska diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173036")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na rakometni natprevari"));
        diplomska1.setScope(new Scope("Masinsko ucenje"));
        diplomska1.setDescription(new Description("Dobar description"));
        diplomska1.setFilePath(new FilePath("diplomska173036.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Odlicna diplomska"));
        diplomska1.setSecondMemberNote(new Note("Super"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173014")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na fudbalski natprevari"));
        diplomska1.setScope(new Scope("Vestacka inteligencija"));
        diplomska1.setDescription(new Description("Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description"));
        diplomska1.setFilePath(new FilePath("diplomska173014.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Super"));
        diplomska1.setSecondMemberNote(new Note("Odlicno"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FEIT"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173036")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na rakometni natprevari"));
        diplomska1.setScope(new Scope("Masinsko ucenje"));
        diplomska1.setDescription(new Description("Dobar description"));
        diplomska1.setFilePath(new FilePath("diplomska173036.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Odlicna diplomska"));
        diplomska1.setSecondMemberNote(new Note("Super"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173014")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na fudbalski natprevari"));
        diplomska1.setScope(new Scope("Vestacka inteligencija"));
        diplomska1.setDescription(new Description("Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description"));
        diplomska1.setFilePath(new FilePath("diplomska173014.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Super"));
        diplomska1.setSecondMemberNote(new Note("Odlicno"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FEIT"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173036")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na rakometni natprevari"));
        diplomska1.setScope(new Scope("Masinsko ucenje"));
        diplomska1.setDescription(new Description("Dobar description"));
        diplomska1.setFilePath(new FilePath("diplomska173036.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Odlicna diplomska"));
        diplomska1.setSecondMemberNote(new Note("Super"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173014")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na fudbalski natprevari"));
        diplomska1.setScope(new Scope("Vestacka inteligencija"));
        diplomska1.setDescription(new Description("Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description"));
        diplomska1.setFilePath(new FilePath("diplomska173014.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Super"));
        diplomska1.setSecondMemberNote(new Note("Odlicno"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FEIT"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173036")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na rakometni natprevari"));
        diplomska1.setScope(new Scope("Masinsko ucenje"));
        diplomska1.setDescription(new Description("Dobar description"));
        diplomska1.setFilePath(new FilePath("diplomska173036.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Odlicna diplomska"));
        diplomska1.setSecondMemberNote(new Note("Super"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173014")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na fudbalski natprevari"));
        diplomska1.setScope(new Scope("Vestacka inteligencija"));
        diplomska1.setDescription(new Description("Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description Dolg description"));
        diplomska1.setFilePath(new FilePath("diplomska173014.pdf"));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Super"));
        diplomska1.setSecondMemberNote(new Note("Odlicno"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FEIT"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);
    }
}
