package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseSessionRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentResponse;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionControllerService;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-sessions")
public class CourseSessionController {

    private final CourseSessionService courseSessionService;
    private final CourseSessionControllerService courseSessionControllerService;

    @Autowired
    public CourseSessionController(CourseSessionService courseSessionService, CourseSessionControllerService courseSessionControllerService) {
        this.courseSessionService = courseSessionService;
        this.courseSessionControllerService = courseSessionControllerService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<CourseSessionResponse>>> getAllCourseSessions() {
        try {
            List<CourseSessionResponse> sessions = courseSessionService.getAllCourseSessions();
            return ResponseEntity.ok(ApiResponse.success(sessions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving course sessions: " + e.getMessage()));
        }
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<CourseSessionResponse>>> getMySessions(
            @AuthenticationPrincipal Long userId) {
        try {
            List<CourseSessionResponse> sessions = courseSessionControllerService.getMySessions(userId);
            return ResponseEntity.ok(ApiResponse.success(sessions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Kurs oturumları alınırken bir hata oluştu: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> getCourseSessionById(@PathVariable Long id) {
        try {
            CourseSessionResponse session = courseSessionService.getCourseSessionById(id);
            return ResponseEntity.ok(ApiResponse.success(session));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error retrieving course session: " + e.getMessage()));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> createCourseSession(
            @Valid @RequestBody CreateCourseSessionRequest request) {
        try {
            CourseSessionResponse session = courseSessionService.createCourseSession(
                    request.getCourseId(),
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getInstructorId(),
                    request.getClassroomId(),
                    request.getMaxStudents()
            );
            return ResponseEntity.ok(ApiResponse.success(session));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error creating course session: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> updateCourseSession(
            @PathVariable Long id,
            @Valid @RequestBody CreateCourseSessionRequest request) {
        try {
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error updating course session: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Void>> deleteCourseSession(@PathVariable Long id) {
        try {
            courseSessionService.deleteCourseSession(id);
            return ResponseEntity.ok(ApiResponse.success());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error deleting course session: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> activateCourseSession(@PathVariable Long id) {
        try {
            CourseSessionResponse session = courseSessionService.activateCourseSession(id);
            return ResponseEntity.ok(ApiResponse.success(session));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error activating course session: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> deactivateCourseSession(@PathVariable Long id) {
        try {
            CourseSessionResponse session = courseSessionService.deactivateCourseSession(id);
            return ResponseEntity.ok(ApiResponse.success(session));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Error deactivating course session: " + e.getMessage()));
        }
    }

    @PostMapping("/{sessionId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> assignStudentToSession(
            @PathVariable Long sessionId,
            @PathVariable Long studentId) {
        try {
            CourseSessionResponse response = courseSessionService.assignStudentToSession(sessionId, studentId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci kurs oturumuna başarıyla atandı", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Öğrenci atanırken bir hata oluştu", null));
        }
    }

    @DeleteMapping("/{sessionId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<CourseSessionResponse>> removeStudentFromSession(
            @PathVariable Long sessionId,
            @PathVariable Long studentId) {
        try {
            CourseSessionResponse response = courseSessionService.removeStudentFromSession(sessionId, studentId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci kurs oturumundan başarıyla kaldırıldı", response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Öğrenci kaldırılırken bir hata oluştu", null));
        }
    }

    @GetMapping("/unassigned-students")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getUnassignedStudents(
            @RequestParam(required = false) Long termId) {
        try {
            List<StudentResponse> students = courseSessionControllerService.getUnassignedStudents(termId);
            return ResponseEntity.ok(ApiResponse.success(students));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}












