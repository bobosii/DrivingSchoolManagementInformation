package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.CreateUserRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.UserResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Employee;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

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

    public AdminServiceImpl(
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

    @Override
    public CourseResponse createCourse(String name, CourseType courseType) {
        return courseService.createCourse(name, courseType);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertUserToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
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
        return convertUserToResponse(savedUser, request);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, CreateUserRequest request) {
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
        return convertUserToResponse(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", userRepository.countByRole(Role.STUDENT));
        stats.put("activeTerms", termRepository.count());
        stats.put("totalInstructors", userRepository.countByRole(Role.INSTRUCTOR));
        return stats;
    }

    private UserResponse convertUserToResponse(User user) {
        return convertUserToResponse(user, null);
    }

    private UserResponse convertUserToResponse(User user, CreateUserRequest request) {
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
        
        // For newly created users, use request data for consistent response
        if (request != null) {
            response.setFirstName(request.getFirstName());
            response.setLastName(request.getLastName());
            response.setEmail(request.getEmail());
            response.setBirthDate(request.getBirthDate());
            response.setFullName(request.getFirstName() + " " + request.getLastName());
        }
        
        return response;
    }
} 