# Data access with JDBC

The project follows the guidelines provided in the Noroff assignment and includes the minimum requirements as described in the Appendix A-B.



**Part A of the assignment**

SQL scripts 01-08 create a SuperheroesDb following requirements in the Appendix A.



**Part B of the assignment**

Directory ChinookJDBC contains a SpringBoot Application in Java. Using JDBC and PostgreSQL the application can be used to manipulate SQL data on the given Chinook Database, as required in the Appendix B of the assignment.



## Getting Started
Clone the repository:
`git clone https://github.com/jimosine/Assignment2.git`
This project uses Gradle Wrapper, so you don't need to have Gradle installed on your machine.

To use the wrapper  to build the project:
`cd ChinookJDBC/Chinook_JDBC`

To build the project:
`./gradlew build` or `bash ./gradlew build`

To run the project:
`./gradlew run` or `bash ./gradlew run`

Alternatively:
Open the project in IntelliJ Ultimate with JDK 17.



## Setting up database connection
Before running the project, go to the file located in src/main/resources/application.properties set up the following:


`spring.datasource.url= jdbc:postgresql://localhost:5432/<nameOfYourChinookDatabase>`

`spring.datasource.username= <DatabaseUsernameLogin>`

`spring.datasource.password= <DatabasePassword>`



## Built With

• IntelliJ IDEA with JDK 17.

• SpringBoot

• PostgreSQL

• Gradle



## Contributors
Stefania van 't Laar-Sapór @stefvtls & Jim Buissink @jimosine



## License
This project is licensed under the MIT License.



## Acknowledgments
Special thanks to the Noroff for providing the opportunity to develop this project.



## Note
This is a student project and it should not be used as a production-ready software.