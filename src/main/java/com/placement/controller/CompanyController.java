package com.placement.controller;

import com.placement.entity.Company;
import com.placement.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // ── Company self-service ──────────────────────────────────────────────────

    @GetMapping("/me")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Company> getMyProfile() {
        return ResponseEntity.ok(companyService.getMyProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Company> updateMyProfile(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateMyProfile(company));
    }

    // ── All authenticated users (read-only) ───────────────────────────────────

    @GetMapping
    public ResponseEntity<List<Company>> getAll() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping("/industry/{industry}")
    public ResponseEntity<List<Company>> getByIndustry(@PathVariable String industry) {
        return ResponseEntity.ok(companyService.getByIndustry(industry));
    }

    // ── Admin only ────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Company> update(@PathVariable Long id, @RequestBody Company company) {
        return ResponseEntity.ok(companyService.updateCompany(id, company));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
