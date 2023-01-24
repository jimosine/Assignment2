package no.noroff.chinook_jdbc.repository;

import no.noroff.chinook_jdbc.models.Customer;
import no.noroff.chinook_jdbc.models.CustomerCountry;

import java.util.List;

public interface CustomerRepository extends CRUDRepository<Customer, Integer> {
    List<Customer> findSubset(int limit, int offset);

    List<Customer> findByName(String name);

    CustomerCountry findCountryWithMostCustomers();
}

