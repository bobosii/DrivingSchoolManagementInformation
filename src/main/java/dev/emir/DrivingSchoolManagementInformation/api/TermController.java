package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.term.CreateTermRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.StudentResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Month;
import dev.emir.DrivingSchoolManagementInformation.service.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/term")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Term>> createTerm(@RequestBody CreateTermRequest request) {
        try {
            Term term = termService.createTerm(
                request.getMonth(),
                request.getYear(),
                request.getQuota()
            );
            return ResponseEntity.ok(new ApiResponse<>(true, "Dönem başarıyla oluşturuldu", term));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<Term>>> getAllTerms() {
        List<Term> terms = termService.getAllTerms();
        return ResponseEntity.ok(new ApiResponse<>(true, "Dönemler başarıyla getirildi", terms));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Term>> getTermById(@PathVariable Long id) {
        Term term = termService.getTermById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Dönem başarıyla getirildi", term));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Term>> updateTerm(
            @PathVariable Long id,
            @RequestBody CreateTermRequest request) {
        try {
            Term term = termService.updateTerm(
                id,
                request.getMonth(),
                request.getYear(),
                request.getQuota()
            );
            return ResponseEntity.ok(new ApiResponse<>(true, "Dönem başarıyla güncellendi", term));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Void>> deleteTerm(@PathVariable Long id) {
        try {
            termService.deleteTerm(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Dönem başarıyla silindi", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PostMapping("/{termId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Term>> assignStudentToTerm(
            @PathVariable Long termId,
            @PathVariable Long studentId) {
        Term term = termService.getTermById(termId);
        Student student = termService.getStudentById(studentId);
        termService.assignStudentToTerm(termId, student);
        return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci döneme başarıyla atandı", term));
    }

    @DeleteMapping("/{termId}/students/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<Term>> removeStudentFromTerm(
            @PathVariable Long termId,
            @PathVariable Long studentId) {
        Term term = termService.getTermById(termId);
        Student student = termService.getStudentById(studentId);
        termService.removeStudentFromTerm(termId, student);
        return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci dönemden başarıyla kaldırıldı", term));
    }

    @GetMapping("/{termId}/students")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getStudentsInTerm(@PathVariable Long termId) {
        List<Student> students = termService.getTermById(termId).getStudents();
        List<StudentResponse> studentResponses = students.stream()
                .map(student -> new StudentResponse(
                        student.getId(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getBirthDate().toString()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "Dönemdeki öğrenciler başarıyla getirildi", studentResponses));
    }
}
