package org.relife.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer preferenceId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "preferred_categories", columnDefinition = "TEXT")
    private String preferredCategories;

    @Column(name = "preferred_locations", columnDefinition = "TEXT")
    private String preferredLocations;

    @Column(name = "urgency_threshold")
    private Integer urgencyThreshold = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Integer getPreferenceId() { return preferenceId; }
    public void setPreferenceId(Integer preferenceId) { this.preferenceId = preferenceId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getPreferredCategories() { return preferredCategories; }
    public void setPreferredCategories(String preferredCategories) { this.preferredCategories = preferredCategories; }

    public String getPreferredLocations() { return preferredLocations; }
    public void setPreferredLocations(String preferredLocations) { this.preferredLocations = preferredLocations; }

    public Integer getUrgencyThreshold() { return urgencyThreshold; }
    public void setUrgencyThreshold(Integer urgencyThreshold) { this.urgencyThreshold = urgencyThreshold; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
