package com.example.demo.dto.response;

public class PersonnelResponse {

    private Long id;
    private String name;
    private String rank;
    private String serviceNumber;
    private Long baseId;
    private String baseName;

    public PersonnelResponse() {}

    public PersonnelResponse(Long id, String name, String rank, String serviceNumber, Long baseId, String baseName) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.serviceNumber = serviceNumber;
        this.baseId = baseId;
        this.baseName = baseName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public String getServiceNumber() { return serviceNumber; }
    public void setServiceNumber(String serviceNumber) { this.serviceNumber = serviceNumber; }
    public Long getBaseId() { return baseId; }
    public void setBaseId(Long baseId) { this.baseId = baseId; }
    public String getBaseName() { return baseName; }
    public void setBaseName(String baseName) { this.baseName = baseName; }
}
