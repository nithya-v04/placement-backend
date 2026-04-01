package com.placement.controller;

import com.placement.dto.ApplicationRequest;
import com.placement.entity.Application;
import com.placement.entity.Application.ApplicationStatus;
import com.placement.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // ── Admin ─────────────────────────────────────────────────────────────────

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Application>> getAll() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
    public ResponseEntity<Application> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.getById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Student ───────────────────────────────────────────────────────────────

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Application>> myApplications() {
        return ResponseEntity.ok(applicationService.getMyApplications());
    }

    @PostMapping("/apply/{jobId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Application> apply(
            @PathVariable Long jobId,
            @RequestBody ApplicationRequest request) {

        return ResponseEntity.ok(
                applicationService.apply(
                        jobId,
                        request.getCoverLetter(),
                        request.getResumeUrl(),
                        request.getGithubUrl(),
                        request.getLinkedinUrl()
                )
        );
    }

    @PatchMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Application> withdraw(@PathVariable Long id) {
        return ResponseEntity.ok(applicationService.withdraw(id));
    }

    // ── Company ───────────────────────────────────────────────────────────────

    @GetMapping("/company")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<Application>> forMyCompany() {
        return ResponseEntity.ok(applicationService.getForMyCompany());
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<List<Application>> byJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getByJob(jobId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<Application> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        ApplicationStatus status = ApplicationStatus.valueOf(body.get("status").toUpperCase());
        String feedback = body.get("feedback");
        return ResponseEntity.ok(applicationService.updateStatus(id, status, feedback));
    }
}
