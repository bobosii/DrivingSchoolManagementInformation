package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.CourseSessionRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseSessionRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-sessions")
public class CourseSessionController {

    private final CourseSessionRepository courseSessionRepository;
    private final CourseSessionService courseSessionService;

    @Autowired
    public CourseSessionController(CourseSessionRepository courseSessionRepository, CourseSessionService courseSessionService) {
        this.courseSessionRepository = courseSessionRepository;
        this.courseSessionService = courseSessionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<CourseSessionResponse>>> getAllCourseSessions() {
        List<CourseSessionResponse> sessions = courseSessionService.getAllCourseSessions();
        return ResponseEntity.ok(ApiResponse.success(sessions));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> getCourseSessionById(@PathVariable Long id) {
        CourseSessionResponse session = courseSessionService.getCourseSessionById(id);
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> createCourseSession(
            @Valid @RequestBody CreateCourseSessionRequest request) {
        CourseSessionResponse session = courseSessionService.createCourseSession(
                request.getCourseId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getInstructorId(),
                request.getClassroomId(),
                request.getMaxStudents()
        );
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> updateCourseSession(
            @PathVariable Long id,
            @Valid @RequestBody CreateCourseSessionRequest request) {
        CourseSessionResponse session = courseSessionService.updateCourseSession(
                id,
                request.getCourseId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getInstructorId(),
                request.getClassroomId(),
                request.getMaxStudents()
        );
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Void>> deleteCourseSession(@PathVariable Long id) {
        courseSessionService.deleteCourseSession(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> activateCourseSession(@PathVariable Long id) {
        CourseSessionResponse session = courseSessionService.activateCourseSession(id);
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> deactivateCourseSession(@PathVariable Long id) {
        CourseSessionResponse session = courseSessionService.deactivateCourseSession(id);
        return ResponseEntity.ok(ApiResponse.success(session));
    }
}












