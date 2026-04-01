package com.placement.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private String email;
    private String role;
    private Long   userId;
    private String message;

    // ── Constructors ──────────────────────────────────────────────────────────

    public LoginResponse() {}

    public LoginResponse(String token, String tokenType, String email,
                         String role, Long userId, String message) {
        this.token     = token;
        this.tokenType = tokenType;
        this.email     = email;
        this.role      = role;
        this.userId    = userId;
        this.message   = message;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public String getToken()                 { return token; }
    public void setToken(String token)       { this.token = token; }

    public String getTokenType()             { return tokenType; }
    public void setTokenType(String t)       { this.tokenType = t; }

    public String getEmail()                 { return email; }
    public void setEmail(String email)       { this.email = email; }

    public String getRole()                  { return role; }
    public void setRole(String role)         { this.role = role; }

    public Long getUserId()                  { return userId; }
    public void setUserId(Long userId)       { this.userId = userId; }

    public String getMessage()               { return message; }
    public void setMessage(String message)   { this.message = message; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String token, tokenType, email, role, message;
        private Long userId;

        private Builder() {}

        public Builder token(String v)      { this.token = v; return this; }
        public Builder tokenType(String v)  { this.tokenType = v; return this; }
        public Builder email(String v)      { this.email = v; return this; }
        public Builder role(String v)       { this.role = v; return this; }
        public Builder userId(Long v)       { this.userId = v; return this; }
        public Builder message(String v)    { this.message = v; return this; }

        public LoginResponse build() {
            return new LoginResponse(token, tokenType, email, role, userId, message);
        }
    }
}
