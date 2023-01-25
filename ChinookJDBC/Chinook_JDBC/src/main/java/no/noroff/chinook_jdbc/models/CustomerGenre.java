package no.noroff.chinook_jdbc.models;

public record CustomerGenre(int customer_id,
                            int genre_id,
                            String name,
                            int frequency
) {
}
