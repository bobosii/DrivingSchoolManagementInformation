package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.EmployeeRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.InstructorRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.EmployeeRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.InstructorRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.CreateUserRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.employee.EmployeeRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.UserResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Course;
import dev.emir.DrivingSchoolManagementInformation.models.Employee;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import dev.emir.DrivingSchoolManagementInformation.service.CourseService;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final InstructorRepository instructorRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final CourseService courseService;

    public AdminController(UserRepository userRepository, InstructorRepository instructorRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, CourseService courseService, CourseSessionService courseSessionService) {
        this.userRepository = userRepository;
        this.instructorRepository = instructorRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseService = courseService;
    }

    @PostMapping("/register/instructor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerInstructor(@RequestBody InstructorRegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.INSTRUCTOR);

        Instructor instructor = new Instructor();
        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setEmail(request.getEmail());
        instructor.setBirthDate(request.getBirthDate());
        instructor.setCertificationNo(request.getCertificationNo());
        instructor.setUser(user);

        user.setInstructor(instructor);
        User savedUser = userRepository.save(user);

        InstructorRegisterResponse response = new InstructorRegisterResponse(
                instructor.getId(),
                instructor.getFirstName(),
                instructor.getLastName(),
                instructor.getEmail(),
                savedUser.getUsername(),
                savedUser.getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeRegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.EMPLOYEE);

        Employee employee = new Employee();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setBirthDate(request.getBirthDate());
        employee.setUser(user);

        user.setEmployee(employee);
        User savedUser = userRepository.save(user);

        EmployeeRegisterResponse response = new EmployeeRegisterResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                savedUser.getUsername(),
                savedUser.getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> adminPanel() {
        return ResponseEntity.ok("Admin paneline hoş geldiniz.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/course/create")
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody CreateCourseRequest request){
        Course created = courseService.createCourse(request.getName(),request.getCourseType());
        ApiResponse<Course> response = new ApiResponse<>(true,"Course created successfully",created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream().map(user -> {
            UserResponse response = new UserResponse();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setRole(user.getRole().toString());

            if (user.getStudent() != null) {
                response.setFirstName(user.getStudent().getFirstName());
                response.setLastName(user.getStudent().getLastName());
                response.setEmail(user.getStudent().getEmail());
                response.setBirthDate(user.getStudent().getBirthDate() != null ? user.getStudent().getBirthDate().toString() : null);
                response.setFullName(user.getStudent().getFullName());
                if (user.getStudent().getTerm() != null) {
                    response.setTermId(user.getStudent().getTerm().getId());
                    response.setTermName(user.getStudent().getTerm().getName());
                }
            } else if (user.getInstructor() != null) {
                response.setFirstName(user.getInstructor().getFirstName());
                response.setLastName(user.getInstructor().getLastName());
                response.setEmail(user.getInstructor().getEmail());
                response.setBirthDate(user.getInstructor().getBirthDate() != null ? user.getInstructor().getBirthDate().toString() : null);
                response.setFullName(user.getInstructor().getFullName());
            } else if (user.getEmployee() != null) {
                response.setFirstName(user.getEmployee().getFirstName());
                response.setLastName(user.getEmployee().getLastName());
                response.setEmail(user.getEmployee().getEmail());
                response.setBirthDate(user.getEmployee().getBirthDate() != null ? user.getEmployee().getBirthDate().toString() : null);
                response.setFullName(user.getEmployee().getFullName());
            }

            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Kullanıcılar başarıyla getirildi", userResponses));
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Username already exists", null));
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));

        String fullName = request.getFirstName() + " " + request.getLastName();
        LocalDate birthDate = null;
        if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) {
            birthDate = LocalDate.parse(request.getBirthDate());
        }

        switch (user.getRole()) {
            case STUDENT -> {
                Student student = new Student();
                student.setFirstName(request.getFirstName());
                student.setLastName(request.getLastName());
                student.setEmail(request.getEmail());
                student.setBirthDate(birthDate);
                student.setUser(user);
                user.setStudent(student);
            }
            case EMPLOYEE -> {
                Employee employee = new Employee();
                employee.setFirstName(request.getFirstName());
                employee.setLastName(request.getLastName());
                employee.setEmail(request.getEmail());
                employee.setBirthDate(birthDate);
                employee.setUser(user);
                user.setEmployee(employee);
            }
            case INSTRUCTOR -> {
                Instructor instructor = new Instructor();
                instructor.setFirstName(request.getFirstName());
                instructor.setLastName(request.getLastName());
                instructor.setEmail(request.getEmail());
                instructor.setBirthDate(birthDate);
                instructor.setUser(user);
                user.setInstructor(instructor);
            }
            default -> {}
        }
        User savedUser = userRepository.save(user);
        UserResponse response = new UserResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            fullName,
            savedUser.getRole().name(),
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getBirthDate()
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", response));
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            user.setRole(Role.valueOf(request.getRole()));
        }
        // Update linked entity if present
        if (user.getStudent() != null) {
            if (request.getFirstName() != null) user.getStudent().setFirstName(request.getFirstName());
            if (request.getLastName() != null) user.getStudent().setLastName(request.getLastName());
            if (request.getEmail() != null) user.getStudent().setEmail(request.getEmail());
            if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) user.getStudent().setBirthDate(java.time.LocalDate.parse(request.getBirthDate()));
        }
        if (user.getEmployee() != null) {
            if (request.getFirstName() != null) user.getEmployee().setFirstName(request.getFirstName());
            if (request.getLastName() != null) user.getEmployee().setLastName(request.getLastName());
            if (request.getEmail() != null) user.getEmployee().setEmail(request.getEmail());
            if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) user.getEmployee().setBirthDate(java.time.LocalDate.parse(request.getBirthDate()));
        }
        if (user.getInstructor() != null) {
            if (request.getFirstName() != null) user.getInstructor().setFirstName(request.getFirstName());
            if (request.getLastName() != null) user.getInstructor().setLastName(request.getLastName());
            if (request.getEmail() != null) user.getInstructor().setEmail(request.getEmail());
            if (request.getBirthDate() != null && !request.getBirthDate().isEmpty()) user.getInstructor().setBirthDate(java.time.LocalDate.parse(request.getBirthDate()));
        }
        User savedUser = userRepository.save(user);
        String fullName = (request.getFirstName() != null ? request.getFirstName() : "") + " " + (request.getLastName() != null ? request.getLastName() : "");
        UserResponse response = new UserResponse(
            savedUser.getId(),
            savedUser.getUsername(),
            fullName.trim(),
            savedUser.getRole().name(),
            request.getFirstName(),
            request.getLastName(),
            request.getEmail(),
            request.getBirthDate()
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", response));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
    }
}

