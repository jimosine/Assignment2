package no.noroff.chinook_jdbc.models;

public record Customer(
        String first_name,
        String last_name,
        String company,
        String address,
        String city,
        String state,
        String country,
        String postal_code,
        String phone,
        String fax,
        String email,
        int support_rep_id){}