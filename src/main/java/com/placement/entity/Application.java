package com.placement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "job_id"}))
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        this.appliedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Application() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                                { return id; }
    public void setId(Long id)                         { this.id = id; }

    public Student getStudent()                        { return student; }
    public void setStudent(Student student)            { this.student = student; }

    public Job getJob()                                { return job; }
    public void setJob(Job job)                        { this.job = job; }

    public ApplicationStatus getStatus()               { return status; }
    public void setStatus(ApplicationStatus status)    { this.status = status; }

    public String getCoverLetter()                     { return coverLetter; }
    public void setCoverLetter(String coverLetter)     { this.coverLetter = coverLetter; }

    public String getFeedback()                        { return feedback; }
    public void setFeedback(String feedback)           { this.feedback = feedback; }

    public LocalDateTime getAppliedAt()                { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt)  { this.appliedAt = appliedAt; }

    public LocalDateTime getUpdatedAt()                { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)  { this.updatedAt = updatedAt; }
    
    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    // ── Enum ─────────────────────────────────────────────────────────────────

    public enum ApplicationStatus {
        APPLIED, UNDER_REVIEW, SHORTLISTED,
        INTERVIEW_SCHEDULED, SELECTED, REJECTED, WITHDRAWN
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Student student;
        private Job job;
        private ApplicationStatus status = ApplicationStatus.APPLIED;
        private String coverLetter;
        private String feedback;
        private String resumeUrl;
        private String githubUrl;
        private String linkedinUrl;

        private Builder() {}

        public Builder student(Student v)            { this.student = v; return this; }
        public Builder job(Job v)                    { this.job = v; return this; }
        public Builder status(ApplicationStatus v)   { this.status = v; return this; }
        public Builder coverLetter(String v)         { this.coverLetter = v; return this; }
        public Builder feedback(String v)            { this.feedback = v; return this; }
        public Builder resumeUrl(String v)         { this.resumeUrl = v; return this; }
        public Builder githubUrl(String v)         { this.githubUrl = v; return this; }
        public Builder linkedinUrl(String v)       { this.linkedinUrl = v; return this; }

        public Application build() {
            Application a = new Application();
            a.student     = this.student;
            a.job         = this.job;
            a.status      = this.status;
            a.coverLetter = this.coverLetter;
            a.feedback    = this.feedback;
            a.resumeUrl = this.resumeUrl;
            a.githubUrl = this.githubUrl;
            a.linkedinUrl = this.linkedinUrl;
            return a;
        }
    }
}
