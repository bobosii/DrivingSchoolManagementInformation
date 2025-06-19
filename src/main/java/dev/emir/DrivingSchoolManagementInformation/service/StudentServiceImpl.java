package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.AppointmentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentAppointmentRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.student.StudentRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.AppointmentInfo;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDetailResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentDashboardResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermRepository termRepository;
    private final StudentCourseSessionService studentCourseSessionService;
    private final AppointmentRepository appointmentRepository;
    private final StudentDashboardService studentDashboardService;

    @Autowired
    public StudentServiceImpl(
            StudentRepository studentRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            TermRepository termRepository,
            StudentCourseSessionService studentCourseSessionService,
            AppointmentRepository appointmentRepository,
            StudentDashboardService studentDashboardService
    ) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.termRepository = termRepository;
        this.studentCourseSessionService = studentCourseSessionService;
        this.appointmentRepository = appointmentRepository;
        this.studentDashboardService = studentDashboardService;
    }

    @Override
    @Transactional
    public StudentRegisterResponse registerStudent(StudentRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
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

        return new StudentRegisterResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                savedUser.getUsername(),
                savedUser.getRole().name(),
                term.getId()
        );
    }

    @Override
    public StudentProfileResponse getStudentProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return ModelMappings.toStudentProfile(student, user);
    }

    @Override
    @Transactional
    public StudentCourseSessionResponse createAppointment(StudentAppointmentRequest request) {
        StudentCourseSession result = studentCourseSessionService.createAppointment(
                request.getStudentId(),
                request.getCourseSessionId()
        );

        return new StudentCourseSessionResponse(
                result.getId(),
                result.getStudent().getId(),
                result.getCourseSession().getId(),
                result.isApproved(),
                result.getAssignedAt(),
                null,
                result.getCourseSession().getCourse().getName()
        );
    }

    @Override
    public StudentDetailResponse getStudentDetails(Long id) {
        // First find the user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found"));

        // Then find the associated student
        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("No student record found for user with ID " + id));

        // Get student's appointments
        Optional<List<Appointment>> appointmentsOpt = appointmentRepository.findByStudentId(student.getId());
        List<Appointment> appointments = appointmentsOpt.orElse(List.of());

        StudentDetailResponse response = new StudentDetailResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getBirthDate().atStartOfDay(),
                student.getTerm() != null ? student.getTerm().getName() : null,
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

        // Set termId if term exists
        if (student.getTerm() != null) {
            response.setTermId(student.getTerm().getId());
        }

        return response;
    }

    @Override
    public List<StudentDetailResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
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
    }

    @Override
    public StudentDashboardResponse getStudentDashboard(Long userId) {
        return studentDashboardService.getDashboardForUserId(userId);
    }

    @Override
    public boolean isCurrentUser(Long studentId, String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null || user.getStudent() == null) {
            return false;
        }

        return user.getStudent().getId().equals(studentId);
    }
} 