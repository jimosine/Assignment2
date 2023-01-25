package no.noroff.chinook_jdbc.repository;

import no.noroff.chinook_jdbc.models.Customer;

import no.noroff.chinook_jdbc.models.CustomerCountry;
import no.noroff.chinook_jdbc.models.CustomerSpender;

import no.noroff.chinook_jdbc.models.CustomerGenre;
import no.noroff.chinook_jdbc.models.CustomerCountry;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepositoryImplementation implements CustomerRepository {

    private final String url;
    private final String username;
    private final String password;

    public CustomerRepositoryImplementation(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        testConnection();
    }


    /**
     This method tests the connection to the database using the provided url, username, and password.
     It creates a connection to the database using the DriverManager class and the provided url, username and password.
     If the connection is successful, it prints "conn" to the console. If it fails, it prints the error message.
     @throws SQLException if there is an error connecting to the database
     */


    public void testConnection() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("conn");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     This method finds all customers from the customer table using a SQL query. The method establishes a connection to
     the database using the DriverManager class and the provided url, username and password. Then, the method
     prepares a SQL statement that selects all customers from the customer table. The method then executes the query
     and processes the result set to create a new List of Customer objects containing the information such as id, first
     name, last name, country, postal code, phone, and email.
     @return A list of all customers in the table
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException
     */
    @Override
    public List<Customer> findAll() {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, email FROM customer";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Customer customer = null;
                customer = new Customer(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("country"),
                        result.getString("postal_code"),
                        result.getString("phone"),
                        result.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    /**
     This method finds a customer by their ID using a SQL query. The method establishes a connection to the database
     using the DriverManager class and the provided url, username and password. Then, the method prepares a SQL statement
     that selects the customer with the matching ID from the customer table. The method then sets the id passed in the
     parameter to the prepared statement, executes the query and processes the result set to create a new Customer object
     containing the customer's information such as id, first name, last name, country, postal code, phone, and email.
     @param id The ID of the customer to find
     @return The customer with the matching ID, or null if no customer with the given ID is found
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException
     */
    @Override
    public Customer findById(Integer id) {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, " +
                "email FROM customer WHERE customer_id = ?";
        Customer customer = null;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                customer = new Customer(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("country"),
                        result.getString("postal_code"),
                        result.getString("phone"),
                        result.getString("email")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    /**
     This method finds a subset of customers from the customer table using a SQL query. The method establishes a
     connection to the database using the DriverManager class and the provided url, username and password. Then, the
     method prepares a SQL statement that selects a limited and offset subset of the customers from the customer table.
     The method then sets the limit and offset passed in the parameter to the prepared statement, executes the query and
     processes the result set to create a new List of Customer objects containing the subset of customers information
     such as id, first name, last name, country, postal code, phone, and email.
     @param limit The maximum number of customers to return
     @param offset The number of customers to skip before starting to return them
     @return A list of customers that is a subset of the customers in the table, with a maximum length of 'limit' and
     starts from the 'offset' number customer_id
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException */

    @Override
    public List<Customer> findSubset(int limit, int offset) {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, " +
                "email FROM customer LIMIT ? OFFSET ?";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Customer customer = null;
                customer = new Customer(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("country"),
                        result.getString("postal_code"),
                        result.getString("phone"),
                        result.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    /**
     This method finds the highest spending customer from the customer and invoice tables, using a SQL query. The method
     establishes a connection to the database using the DriverManager class and the provided url, username and
     password. Then, the method prepares a SQL statement that joins the customer and invoice tables, groups the results
     by customer id, and orders the results by the total amount spent. The method then executes the query and processes
     the result set to create a new CustomerSpender object containing the highest spender's information such as id,
     first name, last name, and total amount spent.
     @return A CustomerSpender object containing the highest spender's information, or null if no customer is found
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException
     */
    @Override
    public CustomerSpender findHighestSpender() {
        String sql = "SELECT customer.customer_id, customer.first_name, customer.last_name, " +
                "SUM(invoice.total) as total FROM customer JOIN invoice ON customer.customer_id = invoice.customer_id" +
                " GROUP BY customer.customer_id ORDER BY total DESC LIMIT 1";
        CustomerSpender customer = null;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                customer = new CustomerSpender(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getDouble("total")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customer;
    }

    /**
     This method finds customers from the customer table by their name using a SQL query. The method establishes a
     connection to the database using the DriverManager class and the provided url, username and password. Then, the
     method prepares a SQL statement that selects customers from the customer table whose last name or first name match
     the provided name (case-insensitive). The method then sets the parameter name to the prepared statement, and
     executes the query, processing the result set to create a new List of Customer objects containing the information
     such as id, first name, last name, country, postal code, phone, and email.
     @param name The name to search for in the customer's first and last name
     @return A list of customers whose first or last name match the provided name
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException
     */
    @Override
    public List<Customer> findByName(String name) {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, email FROM customer " +
                "WHERE last_name LIKE ? OR first_name LIKE ? ";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + name + "%");
            statement.setString(2, "%" + name + "%");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Customer customer = null;
                customer = new Customer(
                        result.getInt("customer_id"),
                        result.getString("first_name"),
                        result.getString("last_name"),
                        result.getString("country"),
                        result.getString("postal_code"),
                        result.getString("phone"),
                        result.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    /**
     This method inserts a new customer into the customer table in the database using a SQL query. The method
     establishes a connection to the database using the DriverManager class and the provided url, username and password.
     Then, the method prepares a SQL statement that inserts a new customer into the customer table. The method sets the
     fields of the customer object passed in the parameter to the prepared statement, and executes the query.
     @param customer The customer object containing the information of the customer to insert
     @return The number of rows affected by the insert query, should be 1 if the customer is inserted successfully
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException
     */
    @Override
    public int insert(Customer customer) {
        String sql = "INSERT INTO customer (first_name, last_name, country, postal_code, phone, email) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        int result = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.first_name());
            statement.setString(2, customer.last_name());
            statement.setString(3, customer.country());
            statement.setString(4, customer.postal_code());
            statement.setString(5, customer.phone());
            statement.setString(6, customer.email());
            // Execute statement
            result = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     This method finds the country with the most customers from the customer table using a SQL query. The method
     establishes a connection to the database using the DriverManager class and the provided url, username and password.
     Then, the method prepares a SQL statement that groups the customers by country and orders the results by the
     frequency of customers of each country, and then limits the result to the top one. The method then executes the
     query and processes the result set to create a new CustomerCountry object containing the country with the most
     customers and the frequency of customers in that country.
     @return A CustomerCountry object containing the country name with the most customers and the frequency of customers
     in that country, or null if no customer is found
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException
     */
    @Override
    public CustomerCountry findCountryWithMostCustomers() {
        CustomerCountry country = null;
        String sql = "SELECT country, COUNT(*) AS frequency " +
                "FROM customer " +
                "GROUP BY country " +
                "ORDER BY frequency DESC " +
                "LIMIT 1";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                country = new CustomerCountry(result.getString("country"), result.getInt("frequency"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return country;
    }

    /**
     This method updates the information of a customer in the customer table using a SQL query.The method establishes a
     connection to the database using the DriverManager class and the provided url, username and password. Then, the
     method prepares a SQL statement that updates the customer's information in the customer table. The method sets the
     fields of the customer object passed in the parameter such as first name, last name, country, postal code, phone,
     and email to the prepared statement and set the customer id as the where clause. Then, the method executes the
     query and returns the number of rows affected by the update query.
     @param customer The customer object containing the updated information
     @return The number of rows affected by the update query
     @throws SQLException if there is an error executing the SQL query
     */
    @Override
    public int update(Customer customer) {
        String sql = "UPDATE customer SET first_name = ?, last_name = ?, " +
                "country =?, postal_code =?, phone=?, email =? WHERE customer_id = ?";
        int result = 0;



        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // Write statement
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, customer.first_name());
            statement.setString(2, customer.last_name());
            statement.setString(3, customer.country());
            statement.setString(4, customer.postal_code());
            statement.setString(5, customer.phone());
            statement.setString(6, customer.email());
            statement.setInt(7, customer.customer_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     This method finds the most popular genre for a customer from the customer, invoice, invoice_line, track and genre
     tables in a database, using a SQL query. The method establishes a connection to the database using the
     DriverManager class and the provided url, username and password. Then, the method prepares a SQL statement
     that joins the customer, invoice, invoice_line, track and genre tables and groups the results by customer id,
     genre id and genre name. Then, the method filters the result by the provided customer id and orders the
     results by the frequency of each genre. The method then limits the result to the top one with ties. The method then
     executes the query and processes the result set to create a new List of CustomerGenre objects containing the
     customer id, genre id, genre name and frequency of the most popular genre for that customer.
     @param customerId The id of the customer
     @return A list of CustomerGenre objects containing the most popular genre for the provided customer id, or an empty
     list if no genre is found
     @throws RuntimeException if there is an error executing the SQL query, such as an SQLException
     */
    @Override
    public List<CustomerGenre> findMostPopularGenreForCustomer(int customerId) {
        List<CustomerGenre> popularGenres = new ArrayList<>();
        String sql = "SELECT customer.customer_id, genre.genre_id, genre.name , COUNT(genre.genre_id) AS frequency\n" +
                "FROM customer\n" +
                "INNER JOIN invoice ON customer.customer_id = invoice.customer_id\n" +
                "INNER JOIN invoice_line ON invoice.invoice_id = invoice_line.invoice_id\n" +
                "INNER JOIN track ON invoice_line.track_id = track.track_id\n" +
                "INNER JOIN genre ON track.genre_id = genre.genre_id\n" +
                "WHERE customer.customer_id = ?\n" +
                "GROUP BY customer.customer_id, genre.genre_id, genre.name\n" +
                "ORDER BY frequency DESC FETCH FIRST 1 ROWS WITH TIES";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, customerId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                CustomerGenre genre = new CustomerGenre(
                        result.getInt("customer_id"),
                        result.getInt("genre_id"),
                        result.getString("name"),
                        result.getInt("frequency")
                );
                popularGenres.add(genre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return popularGenres;
    }
}
