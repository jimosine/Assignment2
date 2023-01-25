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

    @Override   // method to find a customer which first or last name fits specified pattern
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

    //Just return the customer id, would like to also return his total spending's
    //but don't know the result column name for this.
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

    //I don't know exactly what and how they want us to update.
    //This now just changes the customers first name to uppercase.
    //The commented line can change it back to original name.
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

    @Override
    public int delete(Customer object) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

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
