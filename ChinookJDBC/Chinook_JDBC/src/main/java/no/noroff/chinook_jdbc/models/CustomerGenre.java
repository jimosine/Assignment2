package no.noroff.chinook_jdbc.models;

public record CustomerGenre(int customer_id,
                            int invoice_id,
                            int track_id,
                            int genre_id,
                            String name
                            ) { }
