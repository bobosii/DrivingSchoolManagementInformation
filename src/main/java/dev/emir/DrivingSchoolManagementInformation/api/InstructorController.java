package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.instructor.InstructorProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Instructor;
import dev.emir.DrivingSchoolManagementInformation.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {
    // Design Pattern: Service Layer Pattern
    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<Instructor>>> getAllInstructors() {
        try {
            List<Instructor> instructors = instructorService.getAllInstructors();
            return ResponseEntity.ok(new ApiResponse<>(true, "Instructors retrieved successfully", instructors));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving instructors: " + e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<InstructorProfileResponse>> getInstructorProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            InstructorProfileResponse profile = instructorService.getInstructorProfile(username);
            return ResponseEntity.ok(new ApiResponse<>(true, "Instructor profile fetched", profile));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving instructor profile: " + e.getMessage(), null));
        }
    }
}
