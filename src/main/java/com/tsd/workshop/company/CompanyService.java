package com.tsd.workshop.company;

import com.tsd.workshop.company.data.Company;
import com.tsd.workshop.company.data.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Mono<Company> saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public Mono<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public Flux<Company> findAll() {
        return companyRepository.findAll();
    }

    public Mono<Void> deleteById(Long id) {
        return companyRepository.deleteById(id);
    }
}
