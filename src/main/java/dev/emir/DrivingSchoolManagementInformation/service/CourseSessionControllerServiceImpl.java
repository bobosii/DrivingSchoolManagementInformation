package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentResponse;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
// Design Pattern: Service Layer Pattern
@Service
public class CourseSessionControllerServiceImpl implements CourseSessionControllerService {

    private final CourseSessionService courseSessionService;
    private final UserRepository userRepository;

    @Autowired
    public CourseSessionControllerServiceImpl(CourseSessionService courseSessionService, UserRepository userRepository) {
        this.courseSessionService = courseSessionService;
        this.userRepository = userRepository;
    }

    @Override
    public List<CourseSessionResponse> getMySessions(Long userId) {
        // User ID'den User'ı bul
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Instructor ID'sini al
        if (user.getInstructor() == null) {
            throw new RuntimeException("Instructor bilgisi bulunamadı");
        }
        
        Long instructorId = user.getInstructor().getId();
        return courseSessionService.getSessionsForInstructor(instructorId);
    }

    @Override
    public List<StudentResponse> getUnassignedStudents(Long termId) {
        return courseSessionService.getUnassignedStudents(termId);
    }
} 