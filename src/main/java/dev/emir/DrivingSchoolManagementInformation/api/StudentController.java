package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.StudentProfileDTO;
import dev.emir.DrivingSchoolManagementInformation.dto.request.StudentRegisterRequest;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
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

    public StudentController(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, TermRepository termRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.termRepository = termRepository;
    }


    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<StudentProfileDTO> getStudentProfile(Authentication authentication){
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentProfileDTO dto = new StudentProfileDTO(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getBirthDate(),
                student.getTerm() != null ? student.getTerm().getName() : null
        );

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterRequest request){
        if (userRepository.findByUsername(request.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Username already exist");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        Term term = null;
        if (request.getTermId() != null){
            term = termRepository.findById(request.getTermId()).orElse(null);
        }

        Student student = new Student();
        student.setUser(user);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setBirthDate(request.getBirthDate());
        student.setTerm(term);

        studentRepository.save(student);

        user.setStudent(student);
        userRepository.save(user);

        return ResponseEntity.ok("Student registered successfully");
    }

}
