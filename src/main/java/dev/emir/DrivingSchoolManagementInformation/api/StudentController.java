package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentAppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.studentCourseSession.StudentCourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ModelMappings;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.StudentCourseSession;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import dev.emir.DrivingSchoolManagementInformation.service.StudentCourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/student")
public class StudentController {
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final TermRepository termRepository;
    @Autowired
    private StudentCourseSessionService studentCourseSessionService;

    public StudentController(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, TermRepository termRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.termRepository = termRepository;
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<StudentRegisterResponse>> registerStudent(@RequestBody StudentRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);

        Term term = termRepository.findById(request.getTermId())
                .orElseThrow(() -> new RuntimeException("Term not found"));

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setTerm(term);
        student.setUser(user);

        user.setStudent(student);
        User savedUser = userRepository.save(user);

        StudentRegisterResponse responseData = new StudentRegisterResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                savedUser.getUsername(),
                savedUser.getRole().name(),
                term.getId()
        );

        ApiResponse<StudentRegisterResponse> response = new ApiResponse<>(true,"Student registered successfully",responseData);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getStudentProfile(Authentication authentication){
        String username = authentication.getName();

        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentProfileResponse profile = ModelMappings.toStudentProfile(student,user);
        ApiResponse<StudentProfileResponse> response = new ApiResponse<>(true,"Profil bilgileri basariyla getirildi",profile);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/appointment")
    public ResponseEntity<ApiResponse<StudentCourseSessionResponse>> createAppointment(
            @RequestBody StudentAppointmentRequest request
    ){
        StudentCourseSession result = studentCourseSessionService.createAppointment(request.getStudentId(), request.getCourseSessionId());

        StudentCourseSessionResponse responseData = new StudentCourseSessionResponse(
                result.getId(),
                result.getStudent().getId(),
                result.getCourseSession().getId(),
                result.isApproved(),
                result.getAssignedAt(),
                null, // assignedByUsername yok çünkü öğrenci aldı
                result.getCourseSession().getCourse().getName() // ders adı
        );

        ApiResponse<StudentCourseSessionResponse> response = new ApiResponse<>(true,"Appointment created successfully",responseData);
        return ResponseEntity.ok(response);

    }

}
