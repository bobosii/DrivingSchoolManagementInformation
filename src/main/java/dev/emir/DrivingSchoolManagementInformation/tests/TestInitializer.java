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
    @Autowired private final VehicleTypeRepository vehicleTypeRepository;
    @Autowired private final VehicleRepository vehicleRepository;
    @Autowired private final LicenseClassRepository licenseClassRepository;

    public TestInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder, StudentRepository studentRepository,
                           InstructorRepository instructorRepository, EmployeeRepository employeeRepository,
                           CourseRepository courseRepository, ClassroomRepository classroomRepository,
                           CourseSessionRepository courseSessionRepository, AppointmentTypeRepository appointmentTypeRepository,
                           AppointmentRepository appointmentRepository, VehicleTypeRepository vehicleTypeRepository,
                           VehicleRepository vehicleRepository, LicenseClassRepository licenseClassRepository) {
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
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.vehicleRepository = vehicleRepository;
        this.licenseClassRepository = licenseClassRepository;
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

        Course course = createCoursePracticalIfNotExists();
        Course course2 = createCourseTheoreticalIfNotExists();
        Course courseSimulation = createCourseSimulationIfNotExists();
        Classroom classroom = createClassroomIfNotExists();
        CourseSession session = createCourseSessionIfNotExists(course, instructor, classroom);
        CourseSession session1 = createCourseSessionIfNotExists(course2,instructor,classroom);
        CourseSession session2 = createCourseSessionIfNotExists(courseSimulation,instructor,classroom);

        AppointmentType simType = createAppointmentTypeIfNotExists("SIMULATOR");
        AppointmentType drivingType = createAppointmentTypeIfNotExists("DRIVING");

        createAppointmentIfNotExists(student, instructor, session, simType);

        // Create vehicle types
        VehicleType otomobil = createVehicleTypeIfNotExists("Otomobil");
        VehicleType otobus = createVehicleTypeIfNotExists("Otobüs");
        VehicleType tir = createVehicleTypeIfNotExists("TIR");
        VehicleType motosiklet = createVehicleTypeIfNotExists("Motosiklet");
        VehicleType kamyon = createVehicleTypeIfNotExists("Kamyon");
        VehicleType minibus = createVehicleTypeIfNotExists("Minibüs");

        // Create sample vehicles
        createVehicleIfNotExists("34ABC123", "Toyota", true, otomobil);
        createVehicleIfNotExists("34DEF456", "Mercedes", false, otobus);
        createVehicleIfNotExists("34GHI789", "Volvo", false, tir);
        createVehicleIfNotExists("34JKL012", "Honda", false, motosiklet);
        createVehicleIfNotExists("34MNO345", "MAN", false, kamyon);
        createVehicleIfNotExists("34PRS678", "Ford", true, minibus);

        // Create license classes
        createLicenseClassIfNotExists("A", "Motosiklet");
        createLicenseClassIfNotExists("A1", "Hafif Motosiklet");
        createLicenseClassIfNotExists("A2", "Orta Sınıf Motosiklet");
        createLicenseClassIfNotExists("B", "Otomobil");
        createLicenseClassIfNotExists("B1", "Hafif Araç");
        createLicenseClassIfNotExists("C", "Kamyon");
        createLicenseClassIfNotExists("C1", "Hafif Kamyon");
        createLicenseClassIfNotExists("D", "Otobüs");
        createLicenseClassIfNotExists("D1", "Minibüs");
        createLicenseClassIfNotExists("BE", "Römorklu Otomobil");
        createLicenseClassIfNotExists("C1E", "Römorklu Hafif Kamyon");
        createLicenseClassIfNotExists("CE", "Römorklu Kamyon");
        createLicenseClassIfNotExists("D1E", "Römorklu Minibüs");
        createLicenseClassIfNotExists("DE", "Römorklu Otobüs");
        createLicenseClassIfNotExists("M", "Moped");
        createLicenseClassIfNotExists("T", "Traktör");
        createLicenseClassIfNotExists("F", "Engelli Araçları");
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

    private Course createCoursePracticalIfNotExists() {
        return courseRepository.findAll().stream().findFirst().orElseGet(() -> {
            Course c = new Course();
            c.setName("Traffic practice");
            c.setCourseType(CourseType.DRIVING);
            return courseRepository.save(c);
        });
    }
    private Course createCourseTheoreticalIfNotExists() {
        return courseRepository.findAll().stream().findFirst().orElseGet(() -> {
            Course c = new Course();
            c.setName("Traffic Theory");
            c.setCourseType(CourseType.THEORETICAL);
            return courseRepository.save(c);
        });
    }
    private Course createCourseSimulationIfNotExists() {
        return courseRepository.findAll().stream().findFirst().orElseGet(() -> {
            Course c = new Course();
            c.setName("Simulation");
            c.setCourseType(CourseType.SIMULATION);
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
            a.setAppointmentType(type);
            a.setCourseSession(session);
            a.setAppointmentTime(LocalDateTime.now().plusDays(2));
            a.setRequestedAt(LocalDateTime.now());
            a.setStatus(AppointmentStatus.PENDING);
            appointmentRepository.save(a);
        }
    }

    private VehicleType createVehicleTypeIfNotExists(String name) {
        return vehicleTypeRepository.findAll().stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    VehicleType type = new VehicleType();
                    type.setName(name);
                    return vehicleTypeRepository.save(type);
                });
    }

    private void createVehicleIfNotExists(String plate, String brand, boolean automatic, VehicleType type) {
        if (vehicleRepository.findByPlate(plate).isEmpty()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setPlate(plate);
            vehicle.setBrand(brand);
            vehicle.setAutomatic(automatic);
            vehicle.setInspectionDate(LocalDate.now().plusMonths(6));
            vehicle.setInsuranceDate(LocalDate.now().plusYears(1));
            vehicle.setVehicleType(type);
            vehicleRepository.save(vehicle);
        }
    }

    private LicenseClass createLicenseClassIfNotExists(String code, String description) {
        return licenseClassRepository.findByCode(code)
                .orElseGet(() -> {
                    LicenseClass licenseClass = new LicenseClass();
                    licenseClass.setCode(code);
                    licenseClass.setDescription(description);
                    return licenseClassRepository.save(licenseClass);
                });
    }
}
