package no.noroff.chinook_jdbc.repository;

import no.noroff.chinook_jdbc.models.Customer;

import no.noroff.chinook_jdbc.models.CustomerSpender;

import no.noroff.chinook_jdbc.models.CustomerGenre;
import no.noroff.chinook_jdbc.models.CustomerCountry;


import java.util.List;

public interface CustomerRepository extends CRUDRepository<Customer, Integer> {
    List<Customer> findSubset(int limit, int offset);


    CustomerSpender findHighestSpender();

    List<Customer> findByName(String name);


    List<CustomerGenre> findMostPopularGenreForCustomer(int customerId);
    CustomerCountry findCountryWithMostCustomers();


}

