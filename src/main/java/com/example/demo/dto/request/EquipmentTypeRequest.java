package com.example.demo.dto.request;

import com.example.demo.enums.SerializationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EquipmentTypeRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Size(max = 100, message = "Category must be at most 100 characters")
    private String category;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Serialization type is required")
    private SerializationType serializationType = SerializationType.QUANTITY;

    public EquipmentTypeRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public SerializationType getSerializationType() { return serializationType; }
    public void setSerializationType(SerializationType serializationType) { this.serializationType = serializationType; }
}
