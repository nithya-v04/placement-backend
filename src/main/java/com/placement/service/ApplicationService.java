package com.placement.service;

import com.placement.entity.Application;
import com.placement.entity.Application.ApplicationStatus;
import com.placement.entity.Company;
import com.placement.entity.Job;
import com.placement.entity.Student;
import com.placement.entity.User;
import com.placement.repository.ApplicationRepository;
import com.placement.repository.CompanyRepository;
import com.placement.repository.JobRepository;
import com.placement.repository.StudentRepository;
import com.placement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final StudentRepository     studentRepository;
    private final JobRepository         jobRepository;
    private final CompanyRepository     companyRepository;
    private final UserRepository        userRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                               StudentRepository studentRepository,
                               JobRepository jobRepository,
                               CompanyRepository companyRepository,
                               UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository     = studentRepository;
        this.jobRepository         = jobRepository;
        this.companyRepository     = companyRepository;
        this.userRepository        = userRepository;
    }

    // ── Reads ─────────────────────────────────────────────────────────────────

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + id));
    }

    public List<Application> getMyApplications() {
        return applicationRepository.findByStudentId(authenticatedStudent().getId());
    }

    public List<Application> getByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }

    public List<Application> getForMyCompany() {
        return applicationRepository.findByCompanyId(authenticatedCompany().getId());
    }

    // ── Writes ────────────────────────────────────────────────────────────────

    @Transactional
    public Application apply(Long jobId, String coverLetter, String resumeUrl,
                             String githubUrl, String linkedinUrl) {

        System.out.println("STEP 1: entering apply()");

        Student student = authenticatedStudent();
        System.out.println("STEP 2: student found -> " + student.getId());

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        System.out.println("STEP 3: job found -> " + job.getId());

        System.out.println("STEP 4: job status -> " + job.getStatus());
        System.out.println("STEP 5: job deadline -> " + job.getDeadline());
        System.out.println("STEP 6: student cgpa -> " + student.getCgpa());
        System.out.println("STEP 7: cutoff -> " + job.getCgpaCutoff());

        if (job.getStatus() != Job.JobStatus.OPEN) {
            throw new RuntimeException("This job is no longer accepting applications.");
        }

        if (job.getDeadline() != null && job.getDeadline().isBefore(LocalDate.now())) {
            throw new RuntimeException("Application deadline has passed.");
        }

        if (student.getCgpa() < job.getCgpaCutoff()) {
            throw new RuntimeException("CGPA too low");
        }

        if (applicationRepository.existsByStudentIdAndJobId(student.getId(), jobId)) {
            throw new RuntimeException("Already applied");
        }

        System.out.println("STEP 8: creating application");

        Application application = Application.builder()
                .student(student)
                .job(job)
                .coverLetter(coverLetter)
                .resumeUrl(resumeUrl)
                .githubUrl(githubUrl)
                .linkedinUrl(linkedinUrl)
                .status(ApplicationStatus.APPLIED)
                .build();

        System.out.println("STEP 9: saving application");

        return applicationRepository.save(application);
    }
    
    @Transactional
    public Application updateStatus(Long id, ApplicationStatus newStatus, String feedback) {
        Application application = getById(id);
        application.setStatus(newStatus);

        if (feedback != null && !feedback.isBlank()) {
            application.setFeedback(feedback);
        }

        // Auto-mark student as placed when selected
        if (newStatus == ApplicationStatus.SELECTED) {
            Student student = application.getStudent();
            student.setIsPlaced(true);
            studentRepository.save(student);
        }

        return applicationRepository.save(application);
    }

    @Transactional
    public Application withdraw(Long applicationId) {
        Application application = getById(applicationId);
        Student student = authenticatedStudent();

        if (!application.getStudent().getId().equals(student.getId())) {
            throw new RuntimeException("You are not authorised to withdraw this application.");
        }
        if (application.getStatus() == ApplicationStatus.SELECTED) {
            throw new RuntimeException("Cannot withdraw a selected application.");
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        return applicationRepository.save(application);
    }

    @Transactional
    public void delete(Long id) {
        applicationRepository.delete(getById(id));
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Student authenticatedStudent() {
        User user = authenticatedUser();
        return studentRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
    }

    private Company authenticatedCompany() {
        return companyRepository.findByUser(authenticatedUser())
                .orElseThrow(() -> new RuntimeException("Company profile not found"));
    }

    private User authenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
