package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.InstructorRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ModelMappings;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
// Design Pattern: Service Layer Pattern
@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository, UserRepository userRepository) {
        this.instructorRepository = instructorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public InstructorProfileResponse getInstructorProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Instructor instructor = instructorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        return ModelMappings.toInstructorProfile(instructor, user);
    }
} 