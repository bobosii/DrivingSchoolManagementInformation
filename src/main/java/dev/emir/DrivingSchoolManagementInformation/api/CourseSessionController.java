package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.CourseSessionRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.course.CreateCourseSessionRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.CourseSessionResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentResponse;
import dev.emir.DrivingSchoolManagementInformation.service.CourseSessionService;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-sessions")
public class CourseSessionController {

    private final CourseSessionRepository courseSessionRepository;
    private final CourseSessionService courseSessionService;
    private final UserRepository userRepository;

    @Autowired
    public CourseSessionController(CourseSessionRepository courseSessionRepository, CourseSessionService courseSessionService, UserRepository userRepository) {
        this.courseSessionRepository = courseSessionRepository;
        this.courseSessionService = courseSessionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<CourseSessionResponse>>> getAllCourseSessions() {
        List<CourseSessionResponse> sessions = courseSessionService.getAllCourseSessions();
        return ResponseEntity.ok(ApiResponse.success(sessions));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<CourseSessionResponse>>> getMySessions(
            @AuthenticationPrincipal Long userId) {
        try {
            // User ID'den User'ı bul
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
            // Instructor ID'sini al
            if (user.getInstructor() == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Instructor bilgisi bulunamadı"));
            }
            
            Long instructorId = user.getInstructor().getId();
            List<CourseSessionResponse> sessions = courseSessionService.getSessionsForInstructor(instructorId);
            return ResponseEntity.ok(ApiResponse.success(sessions));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Kurs oturumları alınırken bir hata oluştu: " + e.getMessage()));
        }
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
            List<StudentResponse> students = courseSessionService.getUnassignedStudents(termId);
            return ResponseEntity.ok(ApiResponse.success(students));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}












