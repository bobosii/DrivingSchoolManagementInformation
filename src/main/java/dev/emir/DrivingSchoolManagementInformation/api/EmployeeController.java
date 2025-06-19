package dev.emir.DrivingSchoolManagementInformation.api;


import dev.emir.DrivingSchoolManagementInformation.dao.EmployeeRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.UserRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.employee.EmployeeProfileResponse;
import dev.emir.DrivingSchoolManagementInformation.helper.profileMapper.ModelMappings;
import dev.emir.DrivingSchoolManagementInformation.models.Employee;
import dev.emir.DrivingSchoolManagementInformation.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;
    // Design Pattern: Service Layer Pattern
    public EmployeeController(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> getEmployeeProfile(Authentication authentication){
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Employee not found"));


        EmployeeProfileResponse profile = ModelMappings.toEmployeeProfile(employee,user);

        ApiResponse<EmployeeProfileResponse> response = new ApiResponse<>(true,"Employee profile fetched successfully",profile);
        return ResponseEntity.ok(response);
    }
}
