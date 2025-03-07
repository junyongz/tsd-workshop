package com.tsd.workshop.company.web;

import com.tsd.workshop.company.CompanyService;
import com.tsd.workshop.company.data.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping
    public Mono<Company> createCompany(@RequestBody Company company) {
        return companyService.saveCompany(company);
    }

    @GetMapping("/{id}")
    public Mono<Company> getCompany(@PathVariable Long id) {
        return companyService.findById(id);
    }

    @GetMapping
    public Flux<Company> getAllCompanies() {
        return companyService.findAll();
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCompany(@PathVariable Long id) {
        return companyService.deleteById(id);
    }
}