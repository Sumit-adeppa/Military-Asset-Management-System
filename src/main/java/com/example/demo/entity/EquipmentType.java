package com.example.demo.entity;

import com.example.demo.enums.SerializationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_types")
public class EquipmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 100)
    private String category;

    @Size(max = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "serialization_type", nullable = false)
    private SerializationType serializationType = SerializationType.QUANTITY;

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

    public EquipmentType() {}

    public EquipmentType(String name, String category, SerializationType serializationType) {
        this.name = name;
        this.category = category;
        this.serializationType = serializationType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public SerializationType getSerializationType() { return serializationType; }
    public void setSerializationType(SerializationType serializationType) { this.serializationType = serializationType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
