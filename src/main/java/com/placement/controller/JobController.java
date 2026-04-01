package com.placement.controller;

import com.placement.entity.Job;
import com.placement.entity.Job.JobStatus;
import com.placement.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // ── All authenticated users ───────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<Job>> getAll() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Job>> getActive() {
        return ResponseEntity.ok(jobService.getActiveJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Job>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobService.getJobsByCompany(companyId));
    }

    // ── Student ───────────────────────────────────────────────────────────────

    @GetMapping("/eligible")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<Job>> getEligible(@RequestParam Double cgpa) {
        return ResponseEntity.ok(jobService.getEligibleJobs(cgpa));
    }

    // ── Company ───────────────────────────────────────────────────────────────

    @GetMapping("/my-jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<List<Job>> getMyJobs() {
        return ResponseEntity.ok(jobService.getMyCompanyJobs());
    }

    @PostMapping
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<Job> create(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.createJob(job));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<Job> update(@PathVariable Long id, @RequestBody Job job) {
        return ResponseEntity.ok(jobService.updateJob(id, job));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<Job> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        JobStatus status = JobStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(jobService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
