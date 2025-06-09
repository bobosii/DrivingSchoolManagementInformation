package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ExamResponse;
import dev.emir.DrivingSchoolManagementInformation.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ApiResponse getAllExams() {
        List<ExamResponse> exams = examService.getAllExams();
        return ApiResponse.success(exams);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ApiResponse getExamById(@PathVariable Long id) {
        ExamResponse exam = examService.getExamById(id);
        return ApiResponse.success(exam);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR')")
    public ApiResponse createExam(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("examDate") LocalDate examDate,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        ExamResponse exam = examService.createExam(title, description, examDate, file);
        return ApiResponse.success(exam);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR')")
    public ApiResponse updateExam(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("examDate") LocalDate examDate,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        ExamResponse exam = examService.updateExam(id, title, description, examDate, file);
        return ApiResponse.success(exam);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR')")
    public ApiResponse deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ApiResponse.success();
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<ByteArrayResource> downloadExam(@PathVariable Long id) {
        byte[] data = examService.downloadExam(id);
        ExamResponse exam = examService.getExamById(id);
        ByteArrayResource resource = new ByteArrayResource(data);

        // Get file extension from original filename
        String fileExtension = "";
        if (exam.getFileName() != null && exam.getFileName().contains(".")) {
            fileExtension = exam.getFileName().substring(exam.getFileName().lastIndexOf("."));
        }

        // Create filename using exam title and original file extension
        String filename = exam.getTitle() + fileExtension;
        
        // URL encode the filename to handle Turkish characters and spaces
        String encodedFilename = java.net.URLEncoder.encode(filename, java.nio.charset.StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20"); // Replace + with %20 for spaces

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + encodedFilename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(resource);
    }
} 