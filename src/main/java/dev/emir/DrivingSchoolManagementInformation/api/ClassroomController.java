package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.classroom.CreateClassroomRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import dev.emir.DrivingSchoolManagementInformation.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classroom")
public class ClassroomController {
    // Design Pattern: Service Layer Pattern
    @Autowired
    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Classroom>> createClassroom(@RequestBody CreateClassroomRequest request) {
        try {
            Classroom classroom = classroomService.createClassroom(
                request.getName(),
                request.getCapacity(),
                request.getLocation()
            );
            return ResponseEntity.ok(new ApiResponse<>(true, "Classroom created successfully", classroom));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error creating classroom: " + e.getMessage(), null));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','INSTRUCTOR','STUDENT')")
    public ResponseEntity<ApiResponse<List<Classroom>>> getAllClassrooms() {
        try {
            List<Classroom> classrooms = classroomService.getAllClassrooms();
            return ResponseEntity.ok(new ApiResponse<>(true, "Classrooms retrieved successfully", classrooms));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving classrooms: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','INSTRUCTOR','STUDENT')")
    public ResponseEntity<ApiResponse<Classroom>> getClassroomById(@PathVariable Long id) {
        try {
            Classroom classroom = classroomService.getClassroomById(id)
                    .orElseThrow(() -> new RuntimeException("Classroom not found"));
            return ResponseEntity.ok(new ApiResponse<>(true, "Classroom retrieved successfully", classroom));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error retrieving classroom: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Classroom>> updateClassroom(
            @PathVariable Long id,
            @RequestBody CreateClassroomRequest request) {
        try {
            Classroom classroom = classroomService.updateClassroom(
                id,
                request.getName(),
                request.getCapacity(),
                request.getLocation()
            );
            return ResponseEntity.ok(new ApiResponse<>(true, "Classroom updated successfully", classroom));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error updating classroom: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Void>> deleteClassroom(@PathVariable Long id) {
        try {
            classroomService.deleteClassroom(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Sınıf başarıyla silindi", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
