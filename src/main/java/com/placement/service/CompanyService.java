package com.placement.service;

import com.placement.entity.Company;
import com.placement.entity.User;
import com.placement.repository.CompanyRepository;
import com.placement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository    userRepository;

    public CompanyService(CompanyRepository companyRepository,
                          UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository    = userRepository;
    }

    // ── Reads ─────────────────────────────────────────────────────────────────

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    public Company getMyProfile() {
        return companyRepository.findByUser(authenticatedUser())
                .orElseThrow(() -> new RuntimeException("Company profile not found"));
    }

    public List<Company> getByIndustry(String industry) {
        return companyRepository.findByIndustry(industry);
    }

    // ── Writes ────────────────────────────────────────────────────────────────

    @Transactional
    public Company updateMyProfile(Company updated) {
        Company existing = getMyProfile();
        applyUpdates(existing, updated);
        return companyRepository.save(existing);
    }

    @Transactional
    public Company updateCompany(Long id, Company updated) {
        Company existing = getCompanyById(id);
        applyUpdates(existing, updated);
        return companyRepository.save(existing);
    }

    @Transactional
    public void deleteCompany(Long id) {
        companyRepository.delete(getCompanyById(id));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void applyUpdates(Company target, Company src) {
        if (src.getName() != null)          target.setName(src.getName());
        if (src.getIndustry() != null)      target.setIndustry(src.getIndustry());
        if (src.getWebsite() != null)       target.setWebsite(src.getWebsite());
        if (src.getDescription() != null)   target.setDescription(src.getDescription());
        if (src.getLocation() != null)      target.setLocation(src.getLocation());
        if (src.getContactPerson() != null) target.setContactPerson(src.getContactPerson());
        if (src.getContactPhone() != null)  target.setContactPhone(src.getContactPhone());
        if (src.getLogoUrl() != null)       target.setLogoUrl(src.getLogoUrl());
    }

    private User authenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
