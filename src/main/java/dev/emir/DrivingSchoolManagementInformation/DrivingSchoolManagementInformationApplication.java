package dev.emir.DrivingSchoolManagementInformation;

import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DrivingSchoolManagementInformationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrivingSchoolManagementInformationApplication.class, args);

	}
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("student1").isEmpty()) {
				User user = new User();
				user.setUsername("student1");
				user.setPassword(passwordEncoder.encode("1234"));
				user.setRole(Role.STUDENT);
				userRepository.save(user);
			}
		};
	}


}
