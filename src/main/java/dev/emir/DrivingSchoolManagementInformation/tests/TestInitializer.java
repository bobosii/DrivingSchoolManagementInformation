package dev.emir.DrivingSchoolManagementInformation.tests;

import dev.emir.DrivingSchoolManagementInformation.dao.*;
import dev.emir.DrivingSchoolManagementInformation.models.*;
import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(TestInitializer.class);

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final TermRepository termRepository;
    @Autowired
    private final ClassroomRepository classroomRepository;
    @Autowired
    private final CourseRepository courseRepository;
    @Autowired
    private final CourseSessionRepository courseSessionRepository;
    @Autowired
    private final InstructorRepository instructorRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final StudentCourseSessionRepository studentCourseSessionRepository;

    public TestInitializer(UserRepository userRepository, 
                          PasswordEncoder passwordEncoder,
                          TermRepository termRepository,
                          ClassroomRepository classroomRepository,
                          CourseRepository courseRepository,
                          CourseSessionRepository courseSessionRepository,
                          InstructorRepository instructorRepository,
                          EmployeeRepository employeeRepository,
                          StudentRepository studentRepository,
                          StudentCourseSessionRepository studentCourseSessionRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.termRepository = termRepository;
        this.classroomRepository = classroomRepository;
        this.courseRepository = courseRepository;
        this.courseSessionRepository = courseSessionRepository;
        this.instructorRepository = instructorRepository;
        this.employeeRepository = employeeRepository;
        this.studentRepository = studentRepository;
        this.studentCourseSessionRepository = studentCourseSessionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Create Terms
            Term term1 = createTerm("2024 Bahar Dönemi", LocalDate.of(2024, 2, 1), LocalDate.of(2024, 6, 30), 50);
            Term term2 = createTerm("2024 Yaz Dönemi", LocalDate.of(2024, 7, 1), LocalDate.of(2024, 8, 31), 30);

            // Create Classrooms
            Classroom classroom1 = createClassroom("A101", 30);
            Classroom classroom2 = createClassroom("B202", 25);
            Classroom classroom3 = createClassroom("Simülasyon Odası", 10);

            // Create Courses
            Course theoreticalCourse = createCourse("Temel Trafik Eğitimi", CourseType.THEORETICAL);
            Course simulationCourse = createCourse("Simülasyon Eğitimi", CourseType.SIMULATION);
            Course drivingCourse = createCourse("Direksiyon Eğitimi", CourseType.DRIVING);

            // Create Admin User
            createAdminUser();

            // Create Instructor Users
            Instructor instructor1 = createInstructor("instructor1", "123456", "Ahmet", "Yılmaz", "ahmet@example.com", "CERT001");
            Instructor instructor2 = createInstructor("instructor2", "123456", "Ayşe", "Demir", "ayse@example.com", "CERT002");

            // Create Employee Users
            Employee employee1 = createEmployee("employee1", "123456", "Mehmet", "Kaya", "mehmet@example.com", "Kayıt");
            Employee employee2 = createEmployee("employee2", "123456", "Zeynep", "Şahin", "zeynep@example.com", "Muhasebe");

            // Create Student Users
            Student student1 = createStudent("student1", "123456", "Ali", "Öztürk", "ali@example.com", term1);
            Student student2 = createStudent("student2", "123456", "Fatma", "Çelik", "fatma@example.com", term1);
            Student student3 = createStudent("student3", "123456", "Can", "Yıldız", "can@example.com", term2);

            // Create Course Sessions
            CourseSession theoreticalSession = createCourseSession(theoreticalCourse, classroom1, instructor1, 
                LocalDateTime.of(2024, 3, 1, 9, 0), 
                LocalDateTime.of(2024, 3, 1, 12, 0));

            CourseSession simulationSession = createCourseSession(simulationCourse, classroom3, instructor2,
                LocalDateTime.of(2024, 3, 2, 13, 0),
                LocalDateTime.of(2024, 3, 2, 15, 0));

            CourseSession drivingSession = createCourseSession(drivingCourse, null, instructor1,
                LocalDateTime.of(2024, 3, 3, 10, 0),
                LocalDateTime.of(2024, 3, 3, 12, 0));

            // Create Student Course Sessions
            createStudentCourseSession(student1, theoreticalSession, true);
            createStudentCourseSession(student2, simulationSession, false);
            createStudentCourseSession(student3, drivingSession, true);

            logger.info("✅ Test data initialized successfully");
        } catch (Exception e) {
            logger.error("❌ Error initializing test data: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private Term createTerm(String name, LocalDate startDate, LocalDate endDate, int quota) {
        Term term = new Term();
        term.setName(name);
        term.setStartDate(startDate);
        term.setEndDate(endDate);
        term.setQuota(quota);
        term.setStudents(new ArrayList<>());
        return termRepository.save(term);
    }

    private Classroom createClassroom(String name, int capacity) {
        Classroom classroom = new Classroom();
        classroom.setName(name);
        classroom.setCapacity(capacity);
        classroom.setSessions(new ArrayList<>());
        return classroomRepository.save(classroom);
    }

    private Course createCourse(String name, CourseType courseType) {
        Course course = new Course();
        course.setName(name);
        course.setCourseType(courseType);
        course.setSessions(new ArrayList<>());
        return courseRepository.save(course);
    }

    private void createAdminUser() {
        if (!userRepository.findByUsername("admin").isPresent()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            logger.info("✅ Admin user created: username=admin | password=admin123");
        } else {
            logger.info("✅ Admin user already exists: username=admin | password=admin123");
        }
    }

    private Instructor createInstructor(String username, String password, String firstName, String lastName, String email, String certificationNo) {
        if (!userRepository.findByUsername(username).isPresent()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.INSTRUCTOR);

            Instructor instructor = new Instructor();
            instructor.setFirstName(firstName);
            instructor.setLastName(lastName);
            instructor.setEmail(email);
            instructor.setBirthDate(LocalDate.of(1990, 1, 1));
            instructor.setCertificationNo(certificationNo);
            instructor.setUser(user);
            instructor.setSessions(new ArrayList<>());

            user.setInstructor(instructor);
            userRepository.save(user);
            logger.info("✅ Instructor created: username={}", username);
            return instructor;
        }
        return instructorRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
    }

    private Employee createEmployee(String username, String password, String firstName, String lastName, String email, String department) {
        if (!userRepository.findByUsername(username).isPresent()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.EMPLOYEE);

            Employee employee = new Employee();
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setEmail(email);
            employee.setBirthDate(LocalDate.of(1990, 1, 1));
            employee.setDepartment(department);
            employee.setUser(user);

            user.setEmployee(employee);
            userRepository.save(user);
            logger.info("✅ Employee created: username={}", username);
            return employee;
        }
        return employeeRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    private Student createStudent(String username, String password, String firstName, String lastName, String email, Term term) {
        if (!userRepository.findByUsername(username).isPresent()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.STUDENT);

            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setBirthDate(LocalDate.of(2000, 1, 1));
            student.setTerm(term);
            student.setUser(user);
            student.setCourseSessions(new ArrayList<>());
            student.setSimulatorSessions(new ArrayList<>());
            student.setDocuments(new ArrayList<>());
            student.setAppointments(new ArrayList<>());
            student.setExams(new ArrayList<>());
            student.setRequestedLicenseClasses(new ArrayList<>());

            user.setStudent(student);
            userRepository.save(user);
            logger.info("✅ Student created: username={}", username);
            return student;
        }
        return studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    private CourseSession createCourseSession(Course course, Classroom classroom, Instructor instructor, 
                                           LocalDateTime startTime, LocalDateTime endTime) {
        CourseSession session = new CourseSession();
        session.setCourse(course);
        session.setClassroom(classroom);
        session.setInstructor(instructor);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        
        // Update related entities
        if (classroom != null) {
            classroom.getSessions().add(session);
            classroomRepository.save(classroom);
        }
        
        course.getSessions().add(session);
        courseRepository.save(course);
        
        instructor.getSessions().add(session);
        instructorRepository.save(instructor);
        
        return courseSessionRepository.save(session);
    }

    private StudentCourseSession createStudentCourseSession(Student student, CourseSession courseSession, boolean approved) {
        StudentCourseSession studentCourseSession = new StudentCourseSession();
        studentCourseSession.setStudent(student);
        studentCourseSession.setCourseSession(courseSession);
        studentCourseSession.setApproved(approved);
        studentCourseSession.setAssignedAt(LocalDateTime.now());
        
        // Update student's course sessions
        student.getCourseSessions().add(courseSession);
        studentRepository.save(student);
        
        return studentCourseSessionRepository.save(studentCourseSession);
    }
}
