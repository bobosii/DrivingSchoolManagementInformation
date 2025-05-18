package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.classroom.CreateClassroomRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Classroom;
import dev.emir.DrivingSchoolManagementInformation.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classroom")
public class ClassroomController {

    @Autowired
    private final ClassroomService classroomService;


    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Classroom>> createClassroom(
            @RequestBody CreateClassroomRequest request
            ){
        Classroom classroom = classroomService.createClassroom(request.getName(),request.getCapacity());

        ApiResponse<Classroom> response = new ApiResponse<>(true,"Classroom created successfully",classroom);
        return ResponseEntity.ok(response);
    }
}
