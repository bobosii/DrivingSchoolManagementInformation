package dev.emir.DrivingSchoolManagementInformation.tests;

import dev.emir.DrivingSchoolManagementInformation.dao.AppointmentTypeRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.models.AppointmentType;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class TestInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentTypeRepository appointmentTypeRepository;

    @Autowired
    public TestInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AppointmentTypeRepository appointmentTypeRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.appointmentTypeRepository = appointmentTypeRepository;
    }

    @Override
    public void run(String... args) {
        createAppointmentTypesIfNotExists();
        createUserIfNotExists("admin", "admin123", Role.ADMIN);
        createUserIfNotExists("student", "student123", Role.STUDENT);
        createUserIfNotExists("employee", "employee123", Role.EMPLOYEE);
        createUserIfNotExists("instructor", "instructor123", Role.INSTRUCTOR);
    }

    private void createAppointmentTypesIfNotExists() {
        if (appointmentTypeRepository.count() == 0) {
            AppointmentType sim = new AppointmentType();
            sim.setName("SIMULATOR");

            AppointmentType dr = new AppointmentType();
            dr.setName("DRIVING");

            appointmentTypeRepository.saveAll(List.of(sim, dr));
            System.out.println("✅ Appointment types created");
        }
    }

    private void createUserIfNotExists(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            userRepository.save(user);
            System.out.printf("✅ %s user created: username=%s | password=%s%n", role, username, password);
        } else {
            System.out.printf("✅ %s user already exists: username=%s%n", role, username);
        }
    }
}