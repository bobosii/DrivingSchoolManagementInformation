package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.VehicleTypeResponse;
import dev.emir.DrivingSchoolManagementInformation.models.VehicleType;
import dev.emir.DrivingSchoolManagementInformation.dao.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicle-types")
public class VehicleTypeController {
    // Design Pattern: Service Layer Pattern
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<VehicleTypeResponse>>> getAllVehicleTypes() {
        List<VehicleTypeResponse> vehicleTypes = vehicleTypeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Vehicle types retrieved successfully", vehicleTypes));
    }

    private VehicleTypeResponse convertToResponse(VehicleType vehicleType) {
        VehicleTypeResponse response = new VehicleTypeResponse();
        response.setId(vehicleType.getId());
        response.setName(vehicleType.getName());
        return response;
    }
} 