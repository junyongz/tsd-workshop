package com.tsd.workshop.company.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("company")
public class Company {
    @Id
    private Long id;
    private String companyName;
    private Boolean internal = false;

    // Default constructor
    public Company() {}

    // Parameterized constructor
    public Company(String companyName) {
        this.companyName = companyName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", internal=" + internal +
                '}';
    }
}