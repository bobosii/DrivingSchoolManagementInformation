package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dto.request.CreateUserRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.UserResponse;
import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;

import java.util.List;
import java.util.Map;

public interface AdminService {
    
    /**
     * Creates a new course
     * @param name Course name
     * @param courseType Type of course
     * @return CourseResponse with created course details
     */
    CourseResponse createCourse(String name, CourseType courseType);
    
    /**
     * Retrieves all users with their details
     * @return List of UserResponse objects
     */
    List<UserResponse> getAllUsers();
    
    /**
     * Creates a new user with appropriate role-based entity
     * @param request CreateUserRequest containing user details
     * @return UserResponse with created user details
     */
    UserResponse createUser(CreateUserRequest request);
    
    /**
     * Updates an existing user
     * @param id User ID to update
     * @param request CreateUserRequest containing updated user details
     * @return UserResponse with updated user details
     */
    UserResponse updateUser(Long id, CreateUserRequest request);
    
    /**
     * Deletes a user by ID
     * @param id User ID to delete
     */
    void deleteUser(Long id);
    
    /**
     * Retrieves dashboard statistics
     * @return Map containing dashboard statistics
     */
    Map<String, Object> getDashboardStats();
} 