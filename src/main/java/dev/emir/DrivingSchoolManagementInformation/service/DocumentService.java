package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.DocumentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.DocumentCreateRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.DocumentUpdateRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.DocumentResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Document;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DocumentResponse> getDocumentsByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Öğrenci bulunamadı"));
        
        return documentRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DocumentResponse getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        return convertToResponse(document);
    }

    @Transactional
    public DocumentResponse createDocument(MultipartFile file, String type, Long studentId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));

            Document document = new Document();
            document.setType(type);
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setStudent(student);
            document.setFileData(file.getBytes());

            Document savedDocument = documentRepository.save(document);
            return convertToResponse(savedDocument);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Transactional
    public DocumentResponse updateDocument(Long id, DocumentUpdateRequest request) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Belge bulunamadı"));

        document.setType(request.getType());
        document.setStudent(studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new EntityNotFoundException("Öğrenci bulunamadı")));

        Document updatedDocument = documentRepository.save(document);
        return convertToResponse(updatedDocument);
    }

    @Transactional
    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));
        documentRepository.delete(document);
    }

    public ResponseEntity<byte[]> downloadDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(document.getFileType()));
        
        // Create filename as (document type)-(student name)
        String studentName = document.getStudent().getFirstName() + "-" + document.getStudent().getLastName();
        String filename = document.getType() + "-" + studentName;
        
        // Add file extension if it exists in original filename
        String originalFilename = document.getFileName();
        if (originalFilename != null && originalFilename.contains(".")) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            filename += extension;
        }

        // URL encode the filename to handle Turkish characters and spaces
        String encodedFilename = java.net.URLEncoder.encode(filename, java.nio.charset.StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20"); // Replace + with %20 for spaces
        
        // Set Content-Disposition header with filename
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(document.getFileData());
    }

    private DocumentResponse convertToResponse(Document document) {
        DocumentResponse response = new DocumentResponse();
        response.setId(document.getId());
        response.setType(document.getType());
        response.setFileName(document.getFileName());
        response.setStudentId(document.getStudent().getId());
        response.setStudentName(document.getStudent().getFirstName() + " " + document.getStudent().getLastName());
        response.setFileType(document.getFileType());
        response.setFileSize(document.getFileSize());
        return response;
    }
} 