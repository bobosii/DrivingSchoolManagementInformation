package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dao.AppointmentTypeRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.models.AppointmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment-types")
public class AppointmentTypeController {
    private final AppointmentTypeRepository appointmentTypeRepository;
    // Design Pattern: Service Layer Pattern
    @Autowired
    public AppointmentTypeController(AppointmentTypeRepository appointmentTypeRepository) {
        this.appointmentTypeRepository = appointmentTypeRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<AppointmentType>>> getAllAppointmentTypes() {
        List<AppointmentType> appointmentTypes = appointmentTypeRepository.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment types retrieved successfully", appointmentTypes));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentType>> createAppointmentType(@RequestBody AppointmentType appointmentType) {
        AppointmentType savedType = appointmentTypeRepository.save(appointmentType);
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment type created successfully", savedType));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AppointmentType>> updateAppointmentType(
            @PathVariable Long id,
            @RequestBody AppointmentType appointmentType
    ) {
        if (!appointmentTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        appointmentType.setId(id);
        AppointmentType updatedType = appointmentTypeRepository.save(appointmentType);
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment type updated successfully", updatedType));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAppointmentType(@PathVariable Long id) {
        if (!appointmentTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        appointmentTypeRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment type deleted successfully", null));
    }
}