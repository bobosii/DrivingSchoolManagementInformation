package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.StudentRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Student;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Month;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
// Design Pattern: Service Layer Pattern
@Service
public class TermService {

    @Autowired
    private final TermRepository termRepository;

    @Autowired
    private final StudentRepository studentRepository;

    public TermService(TermRepository termRepository, StudentRepository studentRepository) {
        this.termRepository = termRepository;
        this.studentRepository = studentRepository;
    }

    public Term createTerm(Month month, int year, int quota) {
        Term term = new Term();
        term.setMonth(month);
        term.setYear(year);
        term.setQuota(quota);
        return termRepository.save(term);
    }

    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    public Term getTermById(Long id) {
        return termRepository.findById(id).orElseThrow(() -> new RuntimeException("Term not found"));
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Term updateTerm(Long id, Month month, int year, int quota) {
        Term term = getTermById(id);
        term.setMonth(month);
        term.setYear(year);
        term.setQuota(quota);
        return termRepository.save(term);
    }

    public void deleteTerm(Long id) {
        Term term = getTermById(id);
        if (term == null) {
            throw new RuntimeException("Term not found");
        }

        // Önce döneme bağlı öğrencilerin term referansını null yap
        List<Student> students = term.getStudents();
        if (students != null && !students.isEmpty()) {
            for (Student student : students) {
                student.setTerm(null);
                studentRepository.save(student);
            }
        }

        termRepository.delete(term);
    }

    public boolean canAssignStudentToTerm(Long termId) {
        Term term = getTermById(termId);
        return term.getStudents().size() < term.getQuota();
    }

    public void assignStudentToTerm(Long termId, Student student) {
        Term term = getTermById(termId);
        if (!canAssignStudentToTerm(termId)) {
            throw new RuntimeException("Dönem kotası dolu");
        }
        
        // Önce öğrencinin mevcut dönemini temizle
        if (student.getTerm() != null) {
            Term oldTerm = student.getTerm();
            oldTerm.getStudents().remove(student);
            termRepository.save(oldTerm);
        }
        
        // Yeni dönemi ata
        student.setTerm(term);
        studentRepository.save(student);
        
        // Dönemin öğrenci listesini güncelle
        term.getStudents().add(student);
        termRepository.save(term);
    }

    public void removeStudentFromTerm(Long termId, Student student) {
        Term term = getTermById(termId);
        term.getStudents().remove(student);
        student.setTerm(null);
        termRepository.save(term);
    }
}
