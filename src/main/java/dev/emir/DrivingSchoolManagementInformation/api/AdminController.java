package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.EmployeeRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.InstructorRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.EmployeeRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.InstructorRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.employee.EmployeeRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.UserResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Course;
import dev.emir.DrivingSchoolManagementInformation.models.Employee;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import dev.emir.DrivingSchoolManagementInformation.service.CourseService;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
        List<UserResponse> userResponses = users.stream()
                .map(user -> {
                    String fullName = "";
                    if (user.getStudent() != null) {
                        fullName = user.getStudent().getFirstName() + " " + user.getStudent().getLastName();
                    } else if (user.getInstructor() != null) {
                        fullName = user.getInstructor().getFirstName() + " " + user.getInstructor().getLastName();
                    } else if (user.getEmployee() != null) {
                        fullName = user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName();
                    }

                    return new UserResponse(
                            user.getId(),
                            user.getUsername(),
                            fullName,
                            user.getRole().name()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", userResponses));
    }
}

