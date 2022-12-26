package com.example.register.bankrespository;

import com.example.register.userentity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BankRepository extends JpaRepository<Customer,Long> {
    public Customer findByLogin(String login);
    public List<Customer> findAll();

}
