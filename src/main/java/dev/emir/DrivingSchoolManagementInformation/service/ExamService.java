package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.ExamRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.response.ExamResponse;
import dev.emir.DrivingSchoolManagementInformation.models.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public List<ExamResponse> getAllExams() {
        return examRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ExamResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return convertToResponse(exam);
    }

    @Transactional
    public ExamResponse createExam(String title, String description, LocalDate examDate, MultipartFile file) {
        Exam exam = new Exam();
        exam.setTitle(title);
        exam.setDescription(description);
        exam.setExamDate(examDate);

        if (file != null && !file.isEmpty()) {
            try {
                exam.setFileName(file.getOriginalFilename());
                exam.setFileType(file.getContentType());
                exam.setFileSize(file.getSize());
                exam.setFileData(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        Exam savedExam = examRepository.save(exam);
        return convertToResponse(savedExam);
    }

    @Transactional
    public ExamResponse updateExam(Long id, String title, String description, LocalDate examDate, MultipartFile file) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        exam.setTitle(title);
        exam.setDescription(description);
        exam.setExamDate(examDate);

        if (file != null && !file.isEmpty()) {
            try {
                exam.setFileName(file.getOriginalFilename());
                exam.setFileType(file.getContentType());
                exam.setFileSize(file.getSize());
                exam.setFileData(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to store file", e);
            }
        }

        Exam updatedExam = examRepository.save(exam);
        return convertToResponse(updatedExam);
    }

    @Transactional
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    public byte[] downloadExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        if (exam.getFileData() == null) {
            throw new RuntimeException("No file found for this exam");
        }
        return exam.getFileData();
    }

    private ExamResponse convertToResponse(Exam exam) {
        ExamResponse response = new ExamResponse();
        response.setId(exam.getId());
        response.setTitle(exam.getTitle());
        response.setDescription(exam.getDescription());
        response.setExamDate(exam.getExamDate());
        response.setFileName(exam.getFileName());
        response.setFileType(exam.getFileType());
        response.setFileSize(exam.getFileSize());
        return response;
    }
} 