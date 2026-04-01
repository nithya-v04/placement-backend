package com.placement.dto;

import com.placement.entity.User;
import jakarta.validation.constraints.*;

public class RegisterRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotNull(message = "Role is required")
    private User.Role role;

    // Student-specific
    private String  firstName;
    private String  lastName;
    private String  rollNumber;
    private String  department;
    private Double  cgpa;
    private Integer yearOfGraduation;
    private String  phone;
    private String  skills;

    // Company-specific
    private String companyName;
    private String industry;
    private String website;
    private String contactPerson;
    private String location;

    // ── Constructors ──────────────────────────────────────────────────────────

    public RegisterRequest() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getEmail()                         { return email; }
    public void setEmail(String email)               { this.email = email; }

    public String getPassword()                      { return password; }
    public void setPassword(String password)         { this.password = password; }

    public User.Role getRole()                       { return role; }
    public void setRole(User.Role role)              { this.role = role; }

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

    public String getCompanyName()                   { return companyName; }
    public void setCompanyName(String companyName)   { this.companyName = companyName; }

    public String getIndustry()                      { return industry; }
    public void setIndustry(String industry)         { this.industry = industry; }

    public String getWebsite()                       { return website; }
    public void setWebsite(String website)           { this.website = website; }

    public String getContactPerson()                 { return contactPerson; }
    public void setContactPerson(String v)           { this.contactPerson = v; }

    public String getLocation()                      { return location; }
    public void setLocation(String location)         { this.location = location; }
}
