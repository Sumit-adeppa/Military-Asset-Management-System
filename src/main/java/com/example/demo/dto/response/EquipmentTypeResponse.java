package com.example.demo.dto.response;

import com.example.demo.enums.SerializationType;

public class EquipmentTypeResponse {

    private Long id;
    private String name;
    private String category;
    private String description;
    private SerializationType serializationType;

    public EquipmentTypeResponse() {}

    public EquipmentTypeResponse(Long id, String name, String category, String description, SerializationType serializationType) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
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
}
