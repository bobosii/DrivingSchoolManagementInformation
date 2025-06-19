package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.LicenseClassRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.LicenseClassResponse;
import dev.emir.DrivingSchoolManagementInformation.models.LicenseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
// Design Pattern: Service Layer Pattern
@Service
public class LicenseClassService {

    @Autowired
    private LicenseClassRepository licenseClassRepository;

    public List<LicenseClassResponse> getAllLicenseClasses() {
        List<LicenseClass> licenseClasses = licenseClassRepository.findAll();
        return licenseClasses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private LicenseClassResponse convertToResponse(LicenseClass licenseClass) {
        LicenseClassResponse response = new LicenseClassResponse();
        response.setId(licenseClass.getId());
        response.setCode(licenseClass.getCode());
        response.setDescription(licenseClass.getDescription());
        return response;
    }
} 