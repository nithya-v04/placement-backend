package com.placement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private JobType jobType;

    private String location;

    @Column(name = "salary_min")
    private Double salaryMin;

    @Column(name = "salary_max")
    private Double salaryMax;

    @Column(name = "cgpa_cutoff")
    private Double cgpaCutoff;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.OPEN;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    // ── Constructors ──────────────────────────────────────────────────────────

    public Job() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                                { return id; }
    public void setId(Long id)                         { this.id = id; }

    public Company getCompany()                        { return company; }
    public void setCompany(Company company)            { this.company = company; }

    public String getTitle()                           { return title; }
    public void setTitle(String title)                 { this.title = title; }

    public String getDescription()                     { return description; }
    public void setDescription(String description)     { this.description = description; }

    public String getRequirements()                    { return requirements; }
    public void setRequirements(String requirements)   { this.requirements = requirements; }

    public JobType getJobType()                        { return jobType; }
    public void setJobType(JobType jobType)            { this.jobType = jobType; }

    public String getLocation()                        { return location; }
    public void setLocation(String location)           { this.location = location; }

    public Double getSalaryMin()                       { return salaryMin; }
    public void setSalaryMin(Double salaryMin)         { this.salaryMin = salaryMin; }

    public Double getSalaryMax()                       { return salaryMax; }
    public void setSalaryMax(Double salaryMax)         { this.salaryMax = salaryMax; }

    public Double getCgpaCutoff()                      { return cgpaCutoff; }
    public void setCgpaCutoff(Double cgpaCutoff)       { this.cgpaCutoff = cgpaCutoff; }

    public LocalDate getDeadline()                     { return deadline; }
    public void setDeadline(LocalDate deadline)        { this.deadline = deadline; }

    public JobStatus getStatus()                       { return status; }
    public void setStatus(JobStatus status)            { this.status = status; }

    public LocalDateTime getCreatedAt()                { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }

    // ── Enums ─────────────────────────────────────────────────────────────────

    public enum JobType   { FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT }
    public enum JobStatus { OPEN, CLOSED, DRAFT }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Company company;
        private String title, description, requirements, location;
        private JobType jobType;
        private Double salaryMin, salaryMax, cgpaCutoff;
        private LocalDate deadline;
        private JobStatus status = JobStatus.OPEN;

        private Builder() {}

        public Builder company(Company v)       { this.company = v; return this; }
        public Builder title(String v)          { this.title = v; return this; }
        public Builder description(String v)    { this.description = v; return this; }
        public Builder requirements(String v)   { this.requirements = v; return this; }
        public Builder location(String v)       { this.location = v; return this; }
        public Builder jobType(JobType v)       { this.jobType = v; return this; }
        public Builder salaryMin(Double v)      { this.salaryMin = v; return this; }
        public Builder salaryMax(Double v)      { this.salaryMax = v; return this; }
        public Builder cgpaCutoff(Double v)     { this.cgpaCutoff = v; return this; }
        public Builder deadline(LocalDate v)    { this.deadline = v; return this; }
        public Builder status(JobStatus v)      { this.status = v; return this; }

        public Job build() {
            Job j = new Job();
            j.company      = this.company;
            j.title        = this.title;
            j.description  = this.description;
            j.requirements = this.requirements;
            j.location     = this.location;
            j.jobType      = this.jobType;
            j.salaryMin    = this.salaryMin;
            j.salaryMax    = this.salaryMax;
            j.cgpaCutoff   = this.cgpaCutoff;
            j.deadline     = this.deadline;
            j.status       = this.status;
            return j;
        }
    }
}
