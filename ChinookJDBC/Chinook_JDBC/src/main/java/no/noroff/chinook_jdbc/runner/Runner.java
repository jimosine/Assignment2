package no.noroff.chinook_jdbc.runner;

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
        System.out.println(customerRepositoryImpl.findById(1));
    }
}
