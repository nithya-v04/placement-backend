package com.placement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "roll_number", unique = true, nullable = false)
    private String rollNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Double cgpa;

    @Column(name = "year_of_graduation")
    private Integer yearOfGraduation;

    private String phone;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "is_placed")
    private Boolean isPlaced = false;

    @Column(columnDefinition = "TEXT")
    private String about;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Student() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public User getUser()                            { return user; }
    public void setUser(User user)                   { this.user = user; }

    public String getFirstName()                     { return firstName; }
    public void setFirstName(String firstName)       { this.firstName = firstName; }

    public String getLastName()                      { return lastName; }
    public void setLastName(String lastName)         { this.lastName = lastName; }

    public String getRollNumber()                    { return rollNumber; }
    public void setRollNumber(String rollNumber)     { this.rollNumber = rollNumber; }

    public String getDepartment()                    { return department; }
    public void setDepartment(String department)     { this.department = department; }

    public Double getCgpa()                          { return cgpa; }
    public void setCgpa(Double cgpa)                 { this.cgpa = cgpa; }

    public Integer getYearOfGraduation()             { return yearOfGraduation; }
    public void setYearOfGraduation(Integer y)       { this.yearOfGraduation = y; }

    public String getPhone()                         { return phone; }
    public void setPhone(String phone)               { this.phone = phone; }

    public String getSkills()                        { return skills; }
    public void setSkills(String skills)             { this.skills = skills; }

    public String getResumeUrl()                     { return resumeUrl; }
    public void setResumeUrl(String resumeUrl)       { this.resumeUrl = resumeUrl; }

    public Boolean getIsPlaced()                     { return isPlaced; }
    public void setIsPlaced(Boolean isPlaced)        { this.isPlaced = isPlaced; }

    public String getAbout()                         { return about; }
    public void setAbout(String about)               { this.about = about; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private User user;
        private String firstName, lastName, rollNumber, department;
        private Double cgpa;
        private Integer yearOfGraduation;
        private String phone, skills, resumeUrl, about;
        private Boolean isPlaced = false;

        private Builder() {}

        public Builder user(User user)                     { this.user = user; return this; }
        public Builder firstName(String v)                 { this.firstName = v; return this; }
        public Builder lastName(String v)                  { this.lastName = v; return this; }
        public Builder rollNumber(String v)                { this.rollNumber = v; return this; }
        public Builder department(String v)                { this.department = v; return this; }
        public Builder cgpa(Double v)                      { this.cgpa = v; return this; }
        public Builder yearOfGraduation(Integer v)         { this.yearOfGraduation = v; return this; }
        public Builder phone(String v)                     { this.phone = v; return this; }
        public Builder skills(String v)                    { this.skills = v; return this; }
        public Builder resumeUrl(String v)                 { this.resumeUrl = v; return this; }
        public Builder about(String v)                     { this.about = v; return this; }
        public Builder isPlaced(Boolean v)                 { this.isPlaced = v; return this; }

        public Student build() {
            Student s = new Student();
            s.user              = this.user;
            s.firstName         = this.firstName;
            s.lastName          = this.lastName;
            s.rollNumber        = this.rollNumber;
            s.department        = this.department;
            s.cgpa              = this.cgpa;
            s.yearOfGraduation  = this.yearOfGraduation;
            s.phone             = this.phone;
            s.skills            = this.skills;
            s.resumeUrl         = this.resumeUrl;
            s.about             = this.about;
            s.isPlaced          = this.isPlaced;
            return s;
        }
    }
}
