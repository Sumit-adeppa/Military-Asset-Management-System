package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "personnel")
public class Personnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 50)
    @Column(name = "personnel_rank")
    private String rank;

    @NotBlank
    @Size(max = 50)
    @Column(name = "service_number", nullable = false, unique = true)
    private String serviceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_id", nullable = false)
    @NotNull
    private Base base;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
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

    public Personnel() {}

    public Personnel(String name, String rank, String serviceNumber, Base base) {
        this.name = name;
        this.rank = rank;
        this.serviceNumber = serviceNumber;
        this.base = base;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public String getServiceNumber() { return serviceNumber; }
    public void setServiceNumber(String serviceNumber) { this.serviceNumber = serviceNumber; }
    public Base getBase() { return base; }
    public void setBase(Base base) { this.base = base; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
