package mk.ukim.finki.diplomski.aplication;

import mk.ukim.finki.diplomski.aplication.dto.DiplomskaDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaFullDTO;
import mk.ukim.finki.diplomski.aplication.dto.DiplomskaPublicDTO;
import mk.ukim.finki.diplomski.aplication.form.DiplomskaForm;
import mk.ukim.finki.diplomski.aplication.form.OdbranaForm;
import mk.ukim.finki.diplomski.domain.model.Diplomska;
import mk.ukim.finki.diplomski.domain.repository.DiplomskiRepository;
import mk.ukim.finki.diplomski.domain.value.*;
import mk.ukim.finki.sharedkernel.domain.dto.UserDTO;
import mk.ukim.finki.sharedkernel.domain.role.RoleName;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    private final EmailService emailService;
    private final String publicFolderPath;

    public DiplomskiService(DiplomskiRepository diplomskiRepository, UserService userService, RoleService roleService, EmailService emailService, @Value("${public.folder.path}") String folderPath) {
        this.diplomskiRepository = diplomskiRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.emailService = emailService;
        this.publicFolderPath = folderPath;
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

        if (diplomska.getSubmissionDate() == null){
            dto.setDate("");
        } else {
            dto.setDate(diplomska.getSubmissionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        dto.setStatus(Status.descriptionFromNumber(diplomska.getCurrentStatus().getStatus()));
        dto.setDescription(diplomska.getDescription().getDescription());

        if (diplomska.getFilePath() == null){
            dto.setFile("");
        } else {
            dto.setFile(diplomska.getFilePath().getFilePath());
        }

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

        if (diplomska.getOdbranaInfo().getDateTime() == null) {
            dto.setTime("");
            dto.setDate("");
        } else {
            dto.setTime(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            dto.setDate(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        return dto;
    }

    private DiplomskaFullDTO mapDiplomskaFullDTO(Diplomska diplomska){
        DiplomskaBasic tmp = mapDiplomskaBasic(diplomska);
        DiplomskaFullDTO dto = new DiplomskaFullDTO();
        dto.setId(diplomska.id().getId().toString());
        dto.setStudent(tmp.getStudent());
        dto.setMentor(tmp.getMentor());
        dto.setFirstMember(tmp.getFirstMember());
        dto.setSecondMember(tmp.getSecondMember());
        dto.setTitle(tmp.getTitle());
        dto.setDescription(diplomska.getDescription().getDescription());
        dto.setStatus(Status.descriptionFromNumber(diplomska.getCurrentStatus().getStatus()));
        dto.setStatusNumber(diplomska.getCurrentStatus().getStatus());
        dto.setScope(diplomska.getScope().getScope());

        if (diplomska.getSubmissionDate() == null){
            dto.setDate("");
        } else {
            dto.setSubmissionDate(diplomska.getSubmissionDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        }

        if(diplomska.getOdbranaInfo() == null){
            dto.setDate("");
            dto.setTime("");
            dto.setLocation("");
        } else {
            if (diplomska.getOdbranaInfo().getDateTime() == null) {
                dto.setDate("");
                dto.setTime("");
            } else {
                dto.setDate(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                dto.setTime(diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            dto.setLocation(diplomska.getOdbranaInfo().getLocation());
        }

        if (diplomska.getFilePath() == null){
            dto.setFile("");
        } else {
            dto.setFile(diplomska.getFilePath().getFilePath());
        }

        if (diplomska.getFirstMemberNote() == null){
            dto.setFirstMemberNote("");
        } else {
            dto.setFirstMemberNote(diplomska.getFirstMemberNote().getNote());
        }

        if (diplomska.getSecondMemberNote() == null){
            dto.setSecondMemberNote("");
        } else {
            dto.setSecondMemberNote(diplomska.getSecondMemberNote().getNote());
        }

        if (diplomska.getGrade() == null){
            dto.setGrade("");
        } else {
            dto.setGrade(new Grade(diplomska.getGrade().getGrade()).toString());
        }

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


        sendMail(studentId.getId().toString(),"Kreirana diplomska", " Uspesno e dodadena vasata diplomska");
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

    public DiplomskaFullDTO getDiplomska(UUID diplomskaId){
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));
        return mapDiplomskaFullDTO(diplomska);
    }

    public DiplomskaFullDTO getDiplomskaForStudent(UUID studentId) {
        Diplomska diplomska = diplomskiRepository.findDiplomskaByStudentId(new UserId(studentId));
        return mapDiplomskaFullDTO(diplomska);
    }

    public void validateCekor2(UUID diplomskaId, UUID studentId) {
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));

        if (!isInRole(new UserId(studentId), RoleName.STUDENT)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }

        if(!diplomska.getStudentId().getId().equals(studentId)){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        if(diplomska.getCurrentStatus().getStatus()!= 1){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        diplomska.updateStatus();
        diplomskiRepository.saveAndFlush(diplomska);

        //sendMail(diplomska.getMentorId().getId().toString(), "Cekor2", "tekst");
        sendMail(diplomska.getStudentId().getId().toString(), "Cekor2", "tekst");
    }

    public void validateCekor3(UUID diplomskaId, UUID userId){
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));

        if (!isInRole(new UserId(userId), RoleName.ST_SLUZBA)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }
        if(diplomska.getCurrentStatus().getStatus()!= 2){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        diplomska.updateStatus();
        diplomskiRepository.saveAndFlush(diplomska);
        // TODO: send mail - student, mentor, clenovi
        sendMail(diplomska.getStudentId().getId().toString(), "Cekor3", "tekst");
    }

    public void validateCekor3_1(UUID diplomskaId, UUID userId){
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));

        if (!isInRole(new UserId(userId), RoleName.PRODEKAN)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }
        if(diplomska.getCurrentStatus().getStatus()!= 3){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        diplomska.updateStatus();
        diplomskiRepository.saveAndFlush(diplomska);
    }

    public void validateCekor5(UUID diplomskaId, UUID userId){
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));

        if (!diplomska.getMentorId().getId().equals(userId)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }
        if(diplomska.getCurrentStatus().getStatus()!= 5){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        diplomska.updateStatus();
        diplomskiRepository.saveAndFlush(diplomska);
        sendMail(diplomska.getStudentId().getId().toString(), "Cekor5", "tekst");
    }

    public void uploadNote(UUID diplomskaId, String note, UUID userId) {
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));

        if(diplomska.getCurrentStatus().getStatus()!= 5){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        if (diplomska.getFirstMemberId().getId().equals(userId)){
            diplomska.updateFirstMemberNote(note);
        } else if (diplomska.getSecondMemberId().getId().equals(userId)){
            diplomska.updateSecondMemberNote(note);
        } else {
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        diplomskiRepository.saveAndFlush(diplomska);
    }

    public void validateCekor6(UUID diplomskaId, UUID userId){
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));

        if (!isInRole(new UserId(userId), RoleName.ST_SLUZBA)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }
        if(diplomska.getCurrentStatus().getStatus()!= 6){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        diplomska.updateStatus();
        diplomskiRepository.saveAndFlush(diplomska);

        sendMail(diplomska.getStudentId().getId().toString(), "Cekor6", "tekst");
    }

    public void updateOdbranaInfo(OdbranaForm odbranaForm, UUID mentorId){
        UUID diplomskaId = UUID.fromString(odbranaForm.getDiplomskaId());
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));
        if (!diplomska.getMentorId().getId().equals(mentorId)) {
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }

        String dateTime = odbranaForm.getDate() + "-" + odbranaForm.getTime();
        diplomska.updateOdbranaInfo(
                LocalDateTime.parse(dateTime,DateTimeFormatter.ofPattern("d.M.yyyy-HH:mm")),
                odbranaForm.getLocation());
        diplomska.updateStatus();
        diplomskiRepository.saveAndFlush(diplomska);

        generateWordDocument(diplomska);
        sendMail(diplomska.getStudentId().getId().toString(), "Cekor7", "tekst");
    }

    private List<DiplomskaFullDTO> getAllDiplomskiByStatus(Status status) {
        return diplomskiRepository.findAllByCurrentStatusEquals(status)
                .stream()
                .map(this::mapDiplomskaFullDTO)
                .collect(Collectors.toList());
    }

    public List<DiplomskaFullDTO> getDiplomskiForValidationBySluzba(UUID userId) {
        if (!isInRole(new UserId(userId), RoleName.ST_SLUZBA)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }

        return getAllDiplomskiByStatus(new Status(LocalDate.now(), 2));
    }

    public List<DiplomskaFullDTO> getDiplomskiForValidationBySluzbaOdbrana(UUID userId) {
        if (!isInRole(new UserId(userId), RoleName.ST_SLUZBA)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }

        return getAllDiplomskiByStatus(new Status(LocalDate.now(), 6));
    }

    public List<DiplomskaFullDTO> getDiplomskiForValidationByProdekan(UUID userId) {
        if (!isInRole(new UserId(userId), RoleName.PRODEKAN)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }

        return getAllDiplomskiByStatus(new Status(LocalDate.now(), 3));
    }

    public List<DiplomskaFullDTO> getDiplomskiForMentor(UUID mentorId) {
        return diplomskiRepository.findAllByMentorId(new UserId(mentorId))
                .stream()
                .map(this::mapDiplomskaFullDTO)
                .collect(Collectors.toList());
    }

    public void uploadFile(UUID diplomskaId, UUID mentorId, MultipartFile file) {
        Diplomska diplomska = diplomskiRepository.getOne(new DiplomskaId(diplomskaId));

        if (!diplomska.getMentorId().getId().equals(mentorId)){
            throw new RuntimeException("Not authorized");
            // TODO: exception
        }

        if(diplomska.getCurrentStatus().getStatus()!= 4){
            throw new RuntimeException("Not valid");
            // TODO: exception
        }

        String indeks = userService.findUsernameByUserId(diplomska.getStudentId().getId()).getUsername();

        String filePath = publicFolderPath + indeks + ".pdf";
        File f = new File(filePath);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        try (OutputStream os = new FileOutputStream(filePath)){
            os.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        diplomska.updateFilePath(indeks + ".pdf");
        diplomska.updateStatus();
        diplomskiRepository.saveAndFlush(diplomska);

        sendMail(diplomska.getStudentId().getId().toString(), "Cekor4", "tekst");
    }

    public List<DiplomskaFullDTO> getDiplomskiForKomisija(UUID userId) {
        UserId id = new UserId(userId);
        List<Diplomska> diplomski = diplomskiRepository.findAllByFirstMemberId(id);
        diplomski.addAll(diplomskiRepository.findAllBySecondMemberId(id));

        return diplomski.stream()
                .map(this::mapDiplomskaFullDTO)
                .collect(Collectors.toList());
    }

    public void sendMail(String to, String subject, String text) {
        emailService.sendMail(to,subject, text);
    }

    public void generateWordDocument(Diplomska diplomska){
        ObjectFactory factory = Context.getWmlObjectFactory();
        P p = factory.createP();
        R r = factory.createR();
        Text t = factory.createText();
        t.setValue("Записник за дипломска:");
        r.getContent().add(t);
        p.getContent().add(r);
        RPr rpr = factory.createRPr();
        BooleanDefaultTrue b = new BooleanDefaultTrue();
        rpr.setB(b);
        rpr.setI(b);
        rpr.setCaps(b);
        //Color green = factory.createColor();
        //green.setVal("green");
        //rpr.setColor(green);
        r.setRPr(rpr);
        WordprocessingMLPackage wordPackage = null;
        try {
            wordPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        String student = mapStudent(diplomska.getStudentId().getId());
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        mainDocumentPart.addStyledParagraphOfText("Title", "ЗАПИСНИК");
        mainDocumentPart.getContent().add(p);
        mainDocumentPart.addStyledParagraphOfText("p", "Студент: " + student);
        mainDocumentPart.addStyledParagraphOfText("p", "Датум на одбрана: " + diplomska.getOdbranaInfo().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        mainDocumentPart.addStyledParagraphOfText("p", "Локација: " + diplomska.getOdbranaInfo().getLocation());
        mainDocumentPart.addStyledParagraphOfText("p", "Тема: " + diplomska.getTitle().getTitle());
        mainDocumentPart.addStyledParagraphOfText("p", "Област: " + diplomska.getScope().getScope());
        File exportFile = new File(publicFolderPath + (student.split(" -")[0]) + ".docx");
        try {
            wordPackage.save(exportFile);
        } catch (Docx4JException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void generateWordDocument2(){
        Diplomska diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173036")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na rakometni natprevari"));
        diplomska1.setScope(new Scope("Masinsko ucenje"));
        diplomska1.setDescription(new Description("Dobar description"));
        diplomska1.setFilePath(new FilePath(""));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Odlicna diplomska"));
        diplomska1.setSecondMemberNote(new Note("Super"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        generateWordDocument(diplomska1);
    }

    public void populate() {
        diplomskiRepository.deleteAll();
        diplomskiRepository.flush();

        Diplomska diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("dragan.petkov")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("173036")));
        diplomska1.setTitle(new Title("Primena na MU i VI vo analiza na rakometni natprevari"));
        diplomska1.setScope(new Scope("Masinsko ucenje"));
        diplomska1.setDescription(new Description("Dobar description"));
        diplomska1.setFilePath(new FilePath(""));
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
        diplomska1.setFilePath(new FilePath(""));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Super"));
        diplomska1.setSecondMemberNote(new Note("Odlicno"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FEIT"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("stanley.kubrick")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("heywood.floyd")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("david.bowman")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("171999")));
        diplomska1.setTitle(new Title("Креирање реалистични специјални ефекти со помош на ML"));
        diplomska1.setScope(new Scope("Анимација, Машинско Учење"));
        diplomska1.setDescription(new Description("Креирање реалистични специјални ефекти со помош на ML Креирање реалистични специјални ефекти со помош на ML Креирање реалистични специјални ефекти со помош на ML"));
        diplomska1.setFilePath(new FilePath(""));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Super"));
        diplomska1.setSecondMemberNote(new Note("Odlicno"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FEIT"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("carl.sagan")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("goce.gocev")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("171998")));
        diplomska1.setTitle(new Title("Веб апликација за курс по астрофизика"));
        diplomska1.setScope(new Scope("Интернет технологии"));
        diplomska1.setDescription(new Description("Опис опис опис опис опис опис опис опис опис опис опис опис опис"));
        diplomska1.setFilePath(new FilePath(""));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note("Super"));
        diplomska1.setSecondMemberNote(new Note("Odlicno"));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FEIT"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("heywood.floyd")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("vase.vasev")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("goce.gocev")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("171997")));
        diplomska1.setTitle(new Title("Мобилна апликација за јавен превоз"));
        diplomska1.setScope(new Scope("Мобилни апликации"));
        diplomska1.setDescription(new Description("Опис опис опис опис опис опис опис опис опис опис опис опис опис"));
        diplomska1.setFilePath(new FilePath(""));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note(""));
        diplomska1.setSecondMemberNote(new Note(""));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("venko.stojanov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("stanley.kubrick")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("david.bowman")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("171996")));
        diplomska1.setTitle(new Title("Анализа на твитови за претседателските избори"));
        diplomska1.setScope(new Scope("Вештачка интелигенција, Обработка на природни јазици"));
        diplomska1.setDescription(new Description("Опис опис опис опис опис опис опис опис опис опис опис опис опис"));
        diplomska1.setFilePath(new FilePath(""));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note(""));
        diplomska1.setSecondMemberNote(new Note(""));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);

        diplomska1 = new Diplomska();
        diplomska1.setMentorId(new UserId(userService.findUserIdByUsername("toshe.todorov")));
        diplomska1.setFirstMemberId(new UserId(userService.findUserIdByUsername("stanley.kubrick")));
        diplomska1.setSecondMemberId(new UserId(userService.findUserIdByUsername("carl.sagan")));
        diplomska1.setStudentId(new UserId(userService.findUserIdByUsername("171995")));
        diplomska1.setTitle(new Title("Апликација за помош на деца со посебни потреби"));
        diplomska1.setScope(new Scope("Мобилни апликации"));
        diplomska1.setDescription(new Description("Опис опис опис опис опис опис опис опис опис опис опис опис опис"));
        diplomska1.setFilePath(new FilePath(""));
        diplomska1.setGrade(new Grade(10));
        diplomska1.setFirstMemberNote(new Note(""));
        diplomska1.setSecondMemberNote(new Note(""));
        diplomska1.setSubmissionDate(LocalDate.now());
        diplomska1.setOdbranaInfo(new Odbrana(LocalDateTime.now(), "Amfiteatar na FINKI"));
        diplomska1.setCurrentStatus(new Status(LocalDate.now(), 7));
        diplomskiRepository.saveAndFlush(diplomska1);
    }
}
