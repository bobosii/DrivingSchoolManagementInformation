package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.AppointmentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentAppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDetailResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.student.StudentRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.studentCourseSession.StudentCourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ModelMappings;
import dev.emir.DrivingSchoolManagementInformation.models.Appointment;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.StudentCourseSession;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import dev.emir.DrivingSchoolManagementInformation.service.StudentCourseSessionService;
import dev.emir.DrivingSchoolManagementInformation.dto.response.AppointmentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/student")
public class StudentController {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermRepository termRepository;
    private final StudentCourseSessionService studentCourseSessionService;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public StudentController(
            StudentRepository studentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TermRepository termRepository,
            StudentCourseSessionService studentCourseSessionService,
            AppointmentRepository appointmentRepository
    ) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.termRepository = termRepository;
        this.studentCourseSessionService = studentCourseSessionService;
        this.appointmentRepository = appointmentRepository;
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

        ApiResponse<StudentRegisterResponse> response = new ApiResponse<>(true, "Student registered successfully", responseData);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getStudentProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentProfileResponse profile = ModelMappings.toStudentProfile(student, user);
        ApiResponse<StudentProfileResponse> response = new ApiResponse<>(true, "Profile information retrieved successfully", profile);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/appointment")
    public ResponseEntity<ApiResponse<StudentCourseSessionResponse>> createAppointment(
            @RequestBody StudentAppointmentRequest request
    ) {
        StudentCourseSession result = studentCourseSessionService.createAppointment(
                request.getStudentId(),
                request.getCourseSessionId()
        );

        StudentCourseSessionResponse responseData = new StudentCourseSessionResponse(
                result.getId(),
                result.getStudent().getId(),
                result.getCourseSession().getId(),
                result.isApproved(),
                result.getAssignedAt(),
                null,
                result.getCourseSession().getCourse().getName()
        );

        ApiResponse<StudentCourseSessionResponse> response = new ApiResponse<>(true, "Appointment created successfully", responseData);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE') or (hasRole('STUDENT') and @studentController.isCurrentUser(#id))")
    public ResponseEntity<ApiResponse<StudentDetailResponse>> getStudentDetails(@PathVariable Long id) {
        // First find the user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));

        // Then find the associated student
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No student record found for user with ID " + id));

        // Get student's term
        Term term = termRepository.findByStudentsContaining(student)
                .orElse(null);

        // Get student's appointments
        Optional<List<Appointment>> appointmentsOpt = appointmentRepository.findByStudentId(student.getId());
        List<Appointment> appointments = appointmentsOpt.orElse(List.of());

        StudentDetailResponse response = new StudentDetailResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getBirthDate().atStartOfDay(),
                term != null ? term.getName() : "No term assigned",
                appointments.stream()
                        .map(appointment -> new AppointmentInfo(
                                appointment.getId(),
                                appointment.getAppointmentTime(),
                                appointment.getStatus().name(),
                                appointment.getInstructor().getFirstName() + " " + appointment.getInstructor().getLastName(),
                                "-",
                                appointment.getAppointmentType() != null ? appointment.getAppointmentType().getName() : "-"
                        ))
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Student details retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<StudentDetailResponse>>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        List<StudentDetailResponse> studentResponses = students.stream()
            .map(student -> new StudentDetailResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getBirthDate().atStartOfDay(),
                student.getTerm() != null ? student.getTerm().getName() : null,
                null
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenciler başarıyla getirildi", studentResponses));
    }

    public boolean isCurrentUser(Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null || user.getStudent() == null) {
            return false;
        }

        return user.getStudent().getId().equals(studentId);
    }
}
