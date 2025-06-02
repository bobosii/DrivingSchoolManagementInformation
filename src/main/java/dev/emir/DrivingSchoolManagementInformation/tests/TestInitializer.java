package dev.emir.DrivingSchoolManagementInformation.tests;

import dev.emir.DrivingSchoolManagementInformation.dao.*;
import dev.emir.DrivingSchoolManagementInformation.models.*;
import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;
import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class TestInitializer implements CommandLineRunner {

    @Autowired private final UserRepository userRepository;
    @Autowired private final PasswordEncoder passwordEncoder;
    @Autowired private final StudentRepository studentRepository;
    @Autowired private final InstructorRepository instructorRepository;
    @Autowired private final EmployeeRepository employeeRepository;
    @Autowired private final CourseRepository courseRepository;
    @Autowired private final ClassroomRepository classroomRepository;
    @Autowired private final CourseSessionRepository courseSessionRepository;
    @Autowired private final AppointmentTypeRepository appointmentTypeRepository;
    @Autowired private final AppointmentRepository appointmentRepository;

    public TestInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, StudentRepository studentRepository,
                           InstructorRepository instructorRepository, EmployeeRepository employeeRepository,
                           CourseRepository courseRepository, ClassroomRepository classroomRepository,
                           CourseSessionRepository courseSessionRepository, AppointmentTypeRepository appointmentTypeRepository,
                           AppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.employeeRepository = employeeRepository;
        this.courseRepository = courseRepository;
        this.classroomRepository = classroomRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.appointmentTypeRepository = appointmentTypeRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        User studentUser = createUserIfNotExists("student", "student123", Role.STUDENT);
        User studentUser2 = createUserIfNotExists("student2", "student123", Role.STUDENT);
        User instructorUser = createUserIfNotExists("instructor", "instructor123", Role.INSTRUCTOR);
        User employeeUser = createUserIfNotExists("employee", "employee123", Role.EMPLOYEE);
        User adminUser = createUserIfNotExists("admin", "admin123", Role.ADMIN);

        Student student = createStudentIfNotExists(studentUser);
        Student student2 = createStudentIfNotExists(studentUser2);
        Instructor instructor = createInstructorIfNotExists(instructorUser);
        Employee employee = createEmployeeIfNotExists(employeeUser);

        Course course = createCourseIfNotExists();
        Classroom classroom = createClassroomIfNotExists();
        CourseSession session = createCourseSessionIfNotExists(course, instructor, classroom);

        AppointmentType simType = createAppointmentTypeIfNotExists("SIMULATOR");
        AppointmentType drivingType = createAppointmentTypeIfNotExists("DRIVING");

        createAppointmentIfNotExists(student, instructor, session, simType);
    }

    private User createUserIfNotExists(String username, String rawPassword, Role role) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(passwordEncoder.encode(rawPassword));
                    user.setRole(role);
                    return userRepository.save(user);
                });
    }

    private Student createStudentIfNotExists(User user) {
        return studentRepository.findAll().stream()
                .filter(e -> e.getUser() != null && user.getId() != null && user.getId().equals(e.getUser().getId()))
                .findFirst()
                .orElseGet(() -> {
                    Student s = new Student();
                    s.setUser(user);
                    s.setFirstName("Test");
                    s.setLastName("Student");
                    s.setEmail("student@example.com");
                    s.setBirthDate(LocalDate.of(2000, 1, 1));
                    return studentRepository.save(s);
                });
    }

    private Instructor createInstructorIfNotExists(User user) {
        return instructorRepository.findAll().stream()
                .filter(e -> e.getUser() != null && user.getId() != null && user.getId().equals(e.getUser().getId()))
                .findFirst()
                .orElseGet(() -> {
                    Instructor i = new Instructor();
                    i.setUser(user);
                    i.setFirstName("Test");
                    i.setLastName("Instructor");
                    i.setEmail("instructor@example.com");
                    i.setCertificationNo("CERT123");
                    return instructorRepository.save(i);
                });
    }

    private Employee createEmployeeIfNotExists(User user) {
        return employeeRepository.findAll().stream()
                .filter(e -> e.getUser() != null && user.getId() != null && user.getId().equals(e.getUser().getId()))
                .findFirst()
                .orElseGet(() -> {
                    Employee e = new Employee();
                    e.setUser(user);
                    e.setFirstName("Test");
                    e.setLastName("Employee");
                    e.setEmail("employee@example.com");
                    e.setDepartment("Admin");
                    return employeeRepository.save(e);
                });
    }

    private Course createCourseIfNotExists() {
        return courseRepository.findAll().stream().findFirst().orElseGet(() -> {
            Course c = new Course();
            c.setName("Traffic Theory");
            c.setCourseType(CourseType.THEORETICAL);
            return courseRepository.save(c);
        });
    }

    private Classroom createClassroomIfNotExists() {
        return classroomRepository.findAll().stream().findFirst().orElseGet(() -> {
            Classroom cr = new Classroom();
            cr.setName("Room A");
            cr.setCapacity(25);
            return classroomRepository.save(cr);
        });
    }

    private CourseSession createCourseSessionIfNotExists(Course course, Instructor instructor, Classroom classroom) {
        return courseSessionRepository.findAll().stream().findFirst().orElseGet(() -> {
            CourseSession cs = new CourseSession();
            cs.setCourse(course);
            cs.setInstructor(instructor);
            cs.setClassroom(classroom);
            cs.setStartTime(LocalDateTime.now().plusDays(1));
            cs.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
            return courseSessionRepository.save(cs);
        });
    }

    private AppointmentType createAppointmentTypeIfNotExists(String name) {
        return appointmentTypeRepository.findAll().stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    AppointmentType t = new AppointmentType();
                    t.setName(name);
                    return appointmentTypeRepository.save(t);
                });
    }

    private void createAppointmentIfNotExists(Student student, Instructor instructor, CourseSession session, AppointmentType type) {
        if (appointmentRepository.count() == 0) {
            Appointment a = new Appointment();
            a.setStudent(student);
            a.setInstructor(instructor);
            a.setCourseSession(session);
            a.setAppointmentType(type);
            a.setAppointmentTime(LocalDateTime.now().plusDays(2));
            a.setRequestedAt(LocalDateTime.now());
            a.setStatus(AppointmentStatus.PENDING);
            appointmentRepository.save(a);
        }
    }
}
