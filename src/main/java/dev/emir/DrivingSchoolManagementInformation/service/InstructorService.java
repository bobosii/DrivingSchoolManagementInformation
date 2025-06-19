package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;

import java.util.List;
// Design Pattern: Service Layer Pattern
public interface InstructorService {
    
    /**
     * Gets all instructors
     * @return List of Instructor entities
     */
    List<Instructor> getAllInstructors();
    
    /**
     * Gets instructor profile by username
     * @param username Username from authentication
     * @return InstructorProfileResponse with instructor profile details
     */
    InstructorProfileResponse getInstructorProfile(String username);
} 