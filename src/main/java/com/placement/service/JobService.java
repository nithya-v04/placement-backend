package com.placement.service;

import com.placement.entity.Company;
import com.placement.entity.Job;
import com.placement.entity.Job.JobStatus;
import com.placement.entity.User;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class JobService {

    private final JobRepository     jobRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository    userRepository;

    public JobService(JobRepository jobRepository,
                      CompanyRepository companyRepository,
                      UserRepository userRepository) {
        this.jobRepository     = jobRepository;
        this.companyRepository = companyRepository;
        this.userRepository    = userRepository;
    }

    // ── Reads ─────────────────────────────────────────────────────────────────

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public List<Job> getActiveJobs() {
        return jobRepository.findActiveJobs(LocalDate.now());
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
    }

    public List<Job> getJobsByCompany(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }

    public List<Job> getMyCompanyJobs() {
        return jobRepository.findByCompanyId(authenticatedCompany().getId());
    }

    public List<Job> getEligibleJobs(Double cgpa) {
        return jobRepository.findEligibleJobsForStudent(cgpa, LocalDate.now());
    }

    // ── Writes ────────────────────────────────────────────────────────────────

    @Transactional
    public Job createJob(Job job) {
        job.setCompany(authenticatedCompany());
        job.setStatus(JobStatus.OPEN);
        return jobRepository.save(job);
    }

    @Transactional
    public Job updateJob(Long id, Job updated) {
        Job existing = getJobById(id);
        if (updated.getTitle() != null)        existing.setTitle(updated.getTitle());
        if (updated.getDescription() != null)  existing.setDescription(updated.getDescription());
        if (updated.getRequirements() != null) existing.setRequirements(updated.getRequirements());
        if (updated.getJobType() != null)      existing.setJobType(updated.getJobType());
        if (updated.getLocation() != null)     existing.setLocation(updated.getLocation());
        if (updated.getSalaryMin() != null)    existing.setSalaryMin(updated.getSalaryMin());
        if (updated.getSalaryMax() != null)    existing.setSalaryMax(updated.getSalaryMax());
        if (updated.getCgpaCutoff() != null)   existing.setCgpaCutoff(updated.getCgpaCutoff());
        if (updated.getDeadline() != null)     existing.setDeadline(updated.getDeadline());
        return jobRepository.save(existing);
    }

    @Transactional
    public Job updateStatus(Long id, JobStatus status) {
        Job job = getJobById(id);
        job.setStatus(status);
        return jobRepository.save(job);
    }

    @Transactional
    public void deleteJob(Long id) {
        jobRepository.delete(getJobById(id));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Company authenticatedCompany() {
        User user = authenticatedUser();
        return companyRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Company profile not found"));
    }

    private User authenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
