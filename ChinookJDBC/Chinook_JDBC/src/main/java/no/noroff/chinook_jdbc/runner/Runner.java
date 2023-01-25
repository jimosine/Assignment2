package no.noroff.chinook_jdbc.runner;

import no.noroff.chinook_jdbc.models.Customer;
import no.noroff.chinook_jdbc.repository.CustomerRepositoryImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.sql.SQLOutput;

@Component
public class Runner implements ApplicationRunner {
    final
    CustomerRepositoryImplementation customerRepositoryImpl;

    public Runner(CustomerRepositoryImplementation customerRepositoryImpl) {
        this.customerRepositoryImpl = customerRepositoryImpl;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(customerRepositoryImpl.findHighestSpender());
        System.out.println(customerRepositoryImpl.findMostPopularGenreForCustomer(55));
        System.out.println(customerRepositoryImpl.findCountryWithMostCustomers());
        Customer newCustomer = new Customer(1, "Janina", "Nowacka", "Poland", "66-210", "+48123123123", "jan.nowak@gmail.com");
        System.out.println(customerRepositoryImpl.insert(newCustomer));
        System.out.println(customerRepositoryImpl.findByName("Luis"));
        System.out.println(customerRepositoryImpl.findByName("ui"));
        System.out.println(customerRepositoryImpl.findSubset(3, 3));
        System.out.println(customerRepositoryImpl.findById(1));
        System.out.println(customerRepositoryImpl.findAll());
        Customer test = new Customer(59, "John", "Doe", "", "", "", "");
        customerRepositoryImpl.update(test);
        System.out.println(customerRepositoryImpl.findById(59));
    }
}
