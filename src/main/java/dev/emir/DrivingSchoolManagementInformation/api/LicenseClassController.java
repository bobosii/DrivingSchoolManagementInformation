package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.LicenseClassResponse;
import dev.emir.DrivingSchoolManagementInformation.service.LicenseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/license-classes")
public class LicenseClassController {
    // Design Pattern: Service Layer Pattern
    @Autowired
    private LicenseClassService licenseClassService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE', 'INSTRUCTOR')")
    public ResponseEntity<ApiResponse<List<LicenseClassResponse>>> getAllLicenseClasses() {
        List<LicenseClassResponse> licenseClasses = licenseClassService.getAllLicenseClasses();
        return ResponseEntity.ok(new ApiResponse<>(true, "License classes retrieved successfully", licenseClasses));
    }
} 