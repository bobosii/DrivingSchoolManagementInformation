package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.*;
import dev.emir.DrivingSchoolManagementInformation.dto.request.EmployeeRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.InstructorRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.AdminDashboardResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.classroom.ClassroomResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.course.CourseResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.course.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.employee.EmployeeRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.term.TermResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ProfileResponseMapper;
import dev.emir.DrivingSchoolManagementInformation.models.*;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import dev.emir.DrivingSchoolManagementInformation.service.CourseService;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final TermRepository termRepository;
    private final CourseRepository courseRepository;
    private final CourseSessionRepository courseSessionRepository;
    private final ClassroomRepository classroomRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseService courseService;

    @Autowired
    public AdminController(UserRepository userRepository, 
                          InstructorRepository instructorRepository, 
                          EmployeeRepository employeeRepository, 
                          StudentRepository studentRepository,
                          TermRepository termRepository,
                          CourseRepository courseRepository,
                          CourseSessionRepository courseSessionRepository,
                          ClassroomRepository classroomRepository,
                          PasswordEncoder passwordEncoder, 
                          CourseService courseService) {
        this.userRepository = userRepository;
        this.instructorRepository = instructorRepository;
        this.employeeRepository = employeeRepository;
        this.studentRepository = studentRepository;
        this.termRepository = termRepository;
        this.courseRepository = courseRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.classroomRepository = classroomRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboardData(Authentication authentication) {
        System.out.println("=== Admin Dashboard Access Attempt ===");
        System.out.println("Authenticated user: " + authentication.getName());
        System.out.println("User roles: " + authentication.getAuthorities());
        
        try {
            List<Student> students = studentRepository.findAll();
            List<Instructor> instructors = instructorRepository.findAll();
            List<Employee> employees = employeeRepository.findAll();
            List<Term> terms = termRepository.findAll();
            List<Course> courses = courseRepository.findAll();
            List<CourseSession> courseSessions = courseSessionRepository.findAll();
            List<Classroom> classrooms = classroomRepository.findAll();

            System.out.println("Data retrieved - Students: " + students.size() + 
                          ", Instructors: " + instructors.size() + 
                          ", Employees: " + employees.size() + 
                          ", Terms: " + terms.size() + 
                          ", Courses: " + courses.size() + 
                          ", CourseSessions: " + courseSessions.size() + 
                          ", Classrooms: " + classrooms.size());

            AdminDashboardResponse dashboardData = new AdminDashboardResponse(
                students.stream()
                    .map(s -> ProfileResponseMapper.toStudentProfile(s, s.getUser()))
                    .collect(Collectors.toList()),
                instructors.stream()
                    .map(i -> ProfileResponseMapper.toInstructorProfile(i, i.getUser()))
                    .collect(Collectors.toList()),
                employees.stream()
                    .map(e -> ProfileResponseMapper.toEmployeeProfile(e, e.getUser()))
                    .collect(Collectors.toList()),
                terms.stream()
                    .map(t -> new TermResponse(
                        t.getId(),
                        t.getName(),
                        t.getStartDate(),
                        t.getEndDate(),
                        t.getQuota(),
                        t.getStudents() != null ? t.getStudents().size() : 0
                    ))
                    .collect(Collectors.toList()),
                courses.stream()
                    .map(c -> new CourseResponse(c.getId(), c.getName(), c.getCourseType()))
                    .collect(Collectors.toList()),
                courseSessions.stream()
                    .map(cs -> new CourseSessionResponse(
                        cs.getId(),
                        new CourseResponse(cs.getCourse().getId(), cs.getCourse().getName(), cs.getCourse().getCourseType()),
                        cs.getInstructor().getFirstName(),
                        cs.getInstructor().getLastName(),
                        cs.getClassroom() != null ? cs.getClassroom().getName() : null,
                        cs.getStartTime(),
                        cs.getEndTime()
                    ))
                    .collect(Collectors.toList()),
                classrooms.stream()
                    .map(c -> new ClassroomResponse(c.getId(), c.getName(), c.getCapacity()))
                    .collect(Collectors.toList())
            );

            System.out.println("Dashboard data prepared successfully");
            return ResponseEntity.ok(new ApiResponse<>(true, "Dashboard data retrieved successfully", dashboardData));
        } catch (Exception e) {
            System.out.println("Error retrieving dashboard data: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error retrieving dashboard data: " + e.getMessage(), null));
        }
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
        employee.setDepartment(request.getDepartment());
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
    @PostMapping("/course/create")
    public ResponseEntity<ApiResponse<Course>> createCourse(@RequestBody CreateCourseRequest request){
        Course created = courseService.createCourse(request.getName(),request.getCourseType());
        ApiResponse<Course> response = new ApiResponse<>(true,"Course created successfully",created);
        return ResponseEntity.ok(response);
    }
}
