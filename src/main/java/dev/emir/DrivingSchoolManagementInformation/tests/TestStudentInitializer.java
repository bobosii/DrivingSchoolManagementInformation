package dev.emir.DrivingSchoolManagementInformation.tests;

import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class TestStudentInitializer {

//    @Bean
//    public CommandLineRunner initStudent(UserRepository userRepository,
//                                         StudentRepository studentRepository,
//                                         PasswordEncoder passwordEncoder) {
//        return args -> {
//            // 1. User oluştur
//            User user = new User();
//            user.setUsername("student1");
//            user.setPassword(passwordEncoder.encode("1234"));  // Şifreyi encode et
//            user.setRole(Role.STUDENT);
//
//            userRepository.save(user); // User kaydedildiğinde ID atanır
//
//            // 2. Student oluştur ve user ile ilişkilendir
//            Student student = new Student();
//            student.setFirstName("Ali");
//            student.setLastName("Kaya");
//            student.setBirthDate(LocalDate.of(2000, 1, 1));
//            student.setEmail("ali.kaya@example.com");
//            student.setUser(user); // user ilişkilendiriliyor
//
//            studentRepository.save(student);
//
//            // 3. User'a student nesnesini geri ilişkilendir (opsiyonel ama tavsiye edilir)
//            user.setStudent(student);
//            userRepository.save(user);
//
//            System.out.println("✅ Test student oluşturuldu: student1 / 1234");
//        };
//     }
}
