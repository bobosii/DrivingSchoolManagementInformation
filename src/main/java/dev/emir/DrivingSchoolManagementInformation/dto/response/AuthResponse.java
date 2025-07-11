package dev.emir.DrivingSchoolManagementInformation.dto.response;

public class AuthResponse {
    private String token;
    private String username;
    private String role;
    private Long userId;
    private Long linkedEntityId; // Student, Instructor veya Employee ID

    public AuthResponse(String token, String username, String role, Long userId, Long linkedEntityId) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.userId = userId;
        this.linkedEntityId = linkedEntityId;
    }

    // Getters and Setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLinkedEntityId() {
        return linkedEntityId;
    }

    public void setLinkedEntityId(Long linkedEntityId) {
        this.linkedEntityId = linkedEntityId;
    }
}
