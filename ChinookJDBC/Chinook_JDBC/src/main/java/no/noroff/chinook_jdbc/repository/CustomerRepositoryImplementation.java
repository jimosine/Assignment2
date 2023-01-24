package no.noroff.chinook_jdbc.repository;

import no.noroff.chinook_jdbc.models.Customer;
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

    public void testConnection() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("conn");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, email FROM customer";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn =DriverManager.getConnection(url,username, password)){
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()){
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

    @Override
    public Customer findById(Integer id) {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, " +
                "email FROM customer WHERE customer_id = ?";
        Customer customer = null;
        try (Connection conn = DriverManager.getConnection(url, username, password)){
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()){
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

    @Override   // method to find a customer which first or last name fits specified pattern
    public List<Customer> findSubset(int limit, int offset) {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, " +
                "email FROM customer LIMIT ? OFFSET ?";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn =DriverManager.getConnection(url,username, password)){
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet result = statement.executeQuery();
            while (result.next()){
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

    @Override
    public List<Customer> findByName(String name) {
        String sql = "SELECT customer_id, first_name, last_name, country, postal_code, phone, email FROM customer " +
                "WHERE last_name LIKE ? OR first_name LIKE ? ";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn =DriverManager.getConnection(url,username, password)){
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,"%" + name+ "%");
            statement.setString(2,"%"+ name + "%");
            ResultSet result = statement.executeQuery();
            while (result.next()){
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

    @Override
    public int insert(Customer object) {
        return 0;
    }

    @Override
    public CustomerCountry findCountryWithMostCustomers() {
        CustomerCountry country = null;
        String sql = "SELECT country, COUNT(*) AS frequency " +
                "FROM customer " +
                "GROUP BY country " +
                "ORDER BY frequency DESC " +
                "LIMIT 1";
        try (Connection connection = DriverManager.getConnection(url,username,password)){
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                country = new CustomerCountry(result.getString("country"),result.getInt("frequency"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return country;
    }

    @Override
    public int update(Customer object) {
        return 0;
    }

    @Override
    public int delete(Customer object) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

}
