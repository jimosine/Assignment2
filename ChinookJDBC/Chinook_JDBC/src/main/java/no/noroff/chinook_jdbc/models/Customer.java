package no.noroff.chinook_jdbc.models;

public record Customer(
        int customer_id,
        String first_name,
        String last_name,
        String country,
        String postal_code,
        String phone,
        String email) {
}
