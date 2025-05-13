package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.EmployeeRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.InstructorRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.EmployeeRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.InstructorRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.EmployeeRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.InstructorRegisterResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Employee;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    public AdminController(UserRepository userRepository, InstructorRepository instructorRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.instructorRepository = instructorRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
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



}
