package no.noroff.chinook_jdbc.runner;

import no.noroff.chinook_jdbc.models.Customer;
import no.noroff.chinook_jdbc.repository.CustomerRepositoryImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements ApplicationRunner {
    @Autowired
    CustomerRepositoryImplementation customerRepositoryImpl;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("hello world");
        System.out.println(customerRepositoryImpl.findByName("Luis"));
        System.out.println(customerRepositoryImpl.findByName("ui"));
//        System.out.println(customerRepositoryImpl.findSubset(3, 3));
//        System.out.println(customerRepositoryImpl.findById(1));
//        System.out.println(customerRepositoryImpl.findAll());

        //So I guess we need to create a Customer object, which we can just do like this
        //and then we can update.
        Customer test = new Customer(59, "John", "Doe", "", "", "", "");
        customerRepositoryImpl.update(test);
        System.out.println(customerRepositoryImpl.findById(59));
    }
}
