package com.placement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String name;

    private String industry;
    private String website;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "logo_url")
    private String logoUrl;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Company() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                              { return id; }
    public void setId(Long id)                       { this.id = id; }

    public User getUser()                            { return user; }
    public void setUser(User user)                   { this.user = user; }

    public String getName()                          { return name; }
    public void setName(String name)                 { this.name = name; }

    public String getIndustry()                      { return industry; }
    public void setIndustry(String industry)         { this.industry = industry; }

    public String getWebsite()                       { return website; }
    public void setWebsite(String website)           { this.website = website; }

    public String getDescription()                   { return description; }
    public void setDescription(String description)   { this.description = description; }

    public String getLocation()                      { return location; }
    public void setLocation(String location)         { this.location = location; }

    public String getContactPerson()                 { return contactPerson; }
    public void setContactPerson(String v)           { this.contactPerson = v; }

    public String getContactEmail()                  { return contactEmail; }
    public void setContactEmail(String v)            { this.contactEmail = v; }

    public String getContactPhone()                  { return contactPhone; }
    public void setContactPhone(String v)            { this.contactPhone = v; }

    public String getLogoUrl()                       { return logoUrl; }
    public void setLogoUrl(String logoUrl)           { this.logoUrl = logoUrl; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private User user;
        private String name, industry, website, description, location;
        private String contactPerson, contactEmail, contactPhone, logoUrl;

        private Builder() {}

        public Builder user(User user)             { this.user = user; return this; }
        public Builder name(String v)              { this.name = v; return this; }
        public Builder industry(String v)          { this.industry = v; return this; }
        public Builder website(String v)           { this.website = v; return this; }
        public Builder description(String v)       { this.description = v; return this; }
        public Builder location(String v)          { this.location = v; return this; }
        public Builder contactPerson(String v)     { this.contactPerson = v; return this; }
        public Builder contactEmail(String v)      { this.contactEmail = v; return this; }
        public Builder contactPhone(String v)      { this.contactPhone = v; return this; }
        public Builder logoUrl(String v)           { this.logoUrl = v; return this; }

        public Company build() {
            Company c = new Company();
            c.user          = this.user;
            c.name          = this.name;
            c.industry      = this.industry;
            c.website       = this.website;
            c.description   = this.description;
            c.location      = this.location;
            c.contactPerson = this.contactPerson;
            c.contactEmail  = this.contactEmail;
            c.contactPhone  = this.contactPhone;
            c.logoUrl       = this.logoUrl;
            return c;
        }
    }
}
