package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PersonnelRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    private String name;

    @Size(max = 50, message = "Rank must be at most 50 characters")
    private String rank;

    @NotBlank(message = "Service number is required")
    @Size(max = 50, message = "Service number must be at most 50 characters")
    private String serviceNumber;

    @NotNull(message = "Base ID is required")
    private Long baseId;

    public PersonnelRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public String getServiceNumber() { return serviceNumber; }
    public void setServiceNumber(String serviceNumber) { this.serviceNumber = serviceNumber; }
    public Long getBaseId() { return baseId; }
    public void setBaseId(Long baseId) { this.baseId = baseId; }
}
