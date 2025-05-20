package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TermService {
    @Autowired
    private final TermRepository termRepository;

    public TermService(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public Term createTerm(String name, LocalDate startDate, LocalDate endDate, int quota){
        Term term = new Term();
        term.setName(name);
        term.setStartDate(startDate);
        term.setEndDate(endDate);
        term.setQuota(quota);
        return termRepository.save(term);
    }

    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }

    public Term getTermById(Long id) {
        return termRepository.findById(id).orElse(null);
    }

    public Term updateTerm(Long id, Term term) {
        Term existingTerm = termRepository.findById(id).orElse(null);
        if (existingTerm != null) {
            existingTerm.setName(term.getName());
            existingTerm.setStartDate(term.getStartDate());
            existingTerm.setEndDate(term.getEndDate());
            existingTerm.setQuota(term.getQuota());
            return termRepository.save(existingTerm);
        }
        return null;
    }

    public void deleteTerm(Long id) {
        termRepository.deleteById(id);
    }
}
