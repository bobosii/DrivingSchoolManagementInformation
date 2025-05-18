package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.term.CreateTermRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import dev.emir.DrivingSchoolManagementInformation.service.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/term")
public class TermController {
    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Term>> createTerm(
            @RequestBody CreateTermRequest request
            ){
        Term term = termService.createTerm(
                request.getName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getQuota()
        );

        ApiResponse<Term> response = new ApiResponse<>(true,"Term created succesfully",term);

        return ResponseEntity.ok(response);
    }
}
