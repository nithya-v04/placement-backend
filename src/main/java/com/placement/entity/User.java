package com.placement.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public User() {}

    public User(Long id, String email, String password, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ── UserDetails ───────────────────────────────────────────────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public String getUsername()              { return email; }
    @Override public String getPassword()              { return password; }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return true; }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public Role getRole()                  { return role; }
    public void setRole(Role role)         { this.role = role; }

    public LocalDateTime getCreatedAt()             { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // ── Role enum ─────────────────────────────────────────────────────────────

    public enum Role { ADMIN, STUDENT, COMPANY }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Long id;
        private String email;
        private String password;
        private Role role;
        private LocalDateTime createdAt;

        private Builder() {}

        public Builder id(Long id)                         { this.id = id; return this; }
        public Builder email(String email)                 { this.email = email; return this; }
        public Builder password(String password)           { this.password = password; return this; }
        public Builder role(Role role)                     { this.role = role; return this; }
        public Builder createdAt(LocalDateTime createdAt)  { this.createdAt = createdAt; return this; }

        public User build() {
            User u = new User();
            u.id        = this.id;
            u.email     = this.email;
            u.password  = this.password;
            u.role      = this.role;
            u.createdAt = this.createdAt;
            return u;
        }
    }
}
