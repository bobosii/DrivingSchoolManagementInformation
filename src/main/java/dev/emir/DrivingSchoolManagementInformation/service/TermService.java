package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.TermRepository;
import dev.emir.DrivingSchoolManagementInformation.models.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
}
