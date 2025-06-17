package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.*;
import dev.emir.DrivingSchoolManagementInformation.dto.request.*;
import dev.emir.DrivingSchoolManagementInformation.dto.response.*;
import dev.emir.DrivingSchoolManagementInformation.models.*;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import dev.emir.DrivingSchoolManagementInformation.service.CourseService;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TermRepository termRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final CourseService courseService;

    @Autowired
    private final CourseSessionService courseSessionService;

    public AdminController(
            UserRepository userRepository,
            TermRepository termRepository,
            PasswordEncoder passwordEncoder,
            CourseService courseService,
            CourseSessionService courseSessionService) {
        this.userRepository = userRepository;
        this.termRepository = termRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseService = courseService;
        this.courseSessionService = courseSessionService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<String> adminPanel() {
        return ResponseEntity.ok("Admin paneline hoş geldiniz.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/course/create")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(@RequestBody CreateCourseRequest request) {
        CourseResponse created = courseService.createCourse(request.getName(), request.getCourseType());
        ApiResponse<CourseResponse> response = new ApiResponse<>(true, "Course created successfully", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserResponse> userResponses = users.stream()
                    .map(user -> {
                        UserResponse response = new UserResponse();
                        response.setId(user.getId());
                        response.setUsername(user.getUsername());
                        response.setRole(user.getRole().name());
                        if (user.getStudent() != null) {
                            response.setFirstName(user.getStudent().getFirstName());
                            response.setLastName(user.getStudent().getLastName());
                            response.setEmail(user.getStudent().getEmail());
                            response.setBirthDate(user.getStudent().getBirthDate().toString());
                            response.setFullName(user.getStudent().getFullName());
                            if (user.getStudent().getTerm() != null) {
                                response.setTermId(user.getStudent().getTerm().getId());
                                response.setTermName(user.getStudent().getTerm().getName());
                            }
                        } else if (user.getEmployee() != null) {
                            response.setFirstName(user.getEmployee().getFirstName());
                            response.setLastName(user.getEmployee().getLastName());
                            response.setEmail(user.getEmployee().getEmail());
                            response.setFullName(user.getEmployee().getFullName());
                            response.setDepartment(user.getEmployee().getDepartment());
                        } else if (user.getInstructor() != null) {
                            response.setFirstName(user.getInstructor().getFirstName());
                            response.setLastName(user.getInstructor().getLastName());
                            response.setEmail(user.getInstructor().getEmail());
                            response.setFullName(user.getInstructor().getFullName());
                            response.setCertificationNo(user.getInstructor().getCertificationNo());
                        }
                        return response;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", userResponses));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Error retrieving users: " + e.getMessage(), null));
        }
    }

    @PostMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody CreateUserRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Username already exists", null));
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.valueOf(request.getRole()));

            // If role is STUDENT, create a Student record
            if (user.getRole() == Role.STUDENT) {
                Student student = new Student();
                student.setFirstName(request.getFirstName());
                student.setLastName(request.getLastName());
                student.setEmail(request.getEmail());
                student.setBirthDate(LocalDate.parse(request.getBirthDate()));
                student.setUser(user);
                user.setStudent(student);
            }
            // If role is EMPLOYEE, create an Employee record
            if (user.getRole() == Role.EMPLOYEE) {
                Employee employee = new Employee();
                employee.setFirstName(request.getFirstName());
                employee.setLastName(request.getLastName());
                employee.setEmail(request.getEmail());
                employee.setBirthDate(LocalDate.parse(request.getBirthDate()));
                employee.setDepartment(request.getDepartment());
                employee.setUser(user);
                user.setEmployee(employee);
            }
            // If role is INSTRUCTOR, create an Instructor record
            if (user.getRole() == Role.INSTRUCTOR) {
                Instructor instructor = new Instructor();
                instructor.setFirstName(request.getFirstName());
                instructor.setLastName(request.getLastName());
                instructor.setEmail(request.getEmail());
                instructor.setBirthDate(LocalDate.parse(request.getBirthDate()));
                instructor.setCertificationNo(request.getCertificationNo());
                instructor.setUser(user);
                user.setInstructor(instructor);
            }

            User savedUser = userRepository.save(user);
            UserResponse response = new UserResponse();
            response.setId(savedUser.getId());
            response.setUsername(savedUser.getUsername());
            response.setRole(savedUser.getRole().name());
            response.setFirstName(request.getFirstName());
            response.setLastName(request.getLastName());
            response.setEmail(request.getEmail());
            response.setBirthDate(request.getBirthDate());
            response.setFullName(request.getFirstName() + " " + request.getLastName());

            return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error creating user: " + e.getMessage(), null));
        }
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest request) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setUsername(request.getUsername());
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            user.setRole(Role.valueOf(request.getRole()));

            // Update Employee fields if role is EMPLOYEE
            if (user.getRole() == Role.EMPLOYEE && user.getEmployee() != null) {
                Employee employee = user.getEmployee();
                employee.setFirstName(request.getFirstName());
                employee.setLastName(request.getLastName());
                employee.setEmail(request.getEmail());
                employee.setBirthDate(LocalDate.parse(request.getBirthDate()));
                employee.setDepartment(request.getDepartment());
            }
            // Update Instructor fields if role is INSTRUCTOR
            if (user.getRole() == Role.INSTRUCTOR && user.getInstructor() != null) {
                Instructor instructor = user.getInstructor();
                instructor.setFirstName(request.getFirstName());
                instructor.setLastName(request.getLastName());
                instructor.setEmail(request.getEmail());
                instructor.setBirthDate(LocalDate.parse(request.getBirthDate()));
                instructor.setCertificationNo(request.getCertificationNo());
            }

            User savedUser = userRepository.save(user);

            UserResponse response = new UserResponse();
            response.setId(savedUser.getId());
            response.setUsername(savedUser.getUsername());
            response.setRole(savedUser.getRole().name());

            return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error updating user: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error deleting user: " + e.getMessage(), null));
        }
    }

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalStudents", userRepository.countByRole(Role.STUDENT));
            stats.put("activeTerms", termRepository.count());
            stats.put("totalInstructors", userRepository.countByRole(Role.INSTRUCTOR));

            return ResponseEntity.ok(new ApiResponse<>(true, "Stats retrieved successfully", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving stats: " + e.getMessage(), null));
        }
    }
}

