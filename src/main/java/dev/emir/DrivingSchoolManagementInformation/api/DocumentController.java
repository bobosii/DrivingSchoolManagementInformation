package dev.emir.DrivingSchoolManagementInformation.api;

import dev.emir.DrivingSchoolManagementInformation.dto.request.DocumentCreateRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ApiResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.DocumentResponse;
import dev.emir.DrivingSchoolManagementInformation.service.DocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getAllDocuments() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Belgeler başarıyla getirildi", documentService.getAllDocuments()));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STUDENT')")
    public ResponseEntity<ApiResponse<List<DocumentResponse>>> getDocumentsByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Öğrenci belgeleri başarıyla getirildi", documentService.getDocumentsByStudentId(studentId)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STUDENT')")
    public ResponseEntity<ApiResponse<DocumentResponse>> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Belge başarıyla getirildi", documentService.getDocumentById(id)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<DocumentResponse>> createDocument(
            @Valid @ModelAttribute DocumentCreateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Belge başarıyla oluşturuldu", 
            documentService.createDocument(request.getFile(), request.getType(), request.getStudentId())));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<DocumentResponse>> updateDocument(
            @PathVariable Long id,
            @Valid @ModelAttribute DocumentCreateRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Belge başarıyla güncellendi", 
            documentService.updateDocument(id, request.getFile(), request.getType(), request.getStudentId())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Belge başarıyla silindi", null));
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STUDENT')")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        return documentService.downloadDocument(id);
    }
} 