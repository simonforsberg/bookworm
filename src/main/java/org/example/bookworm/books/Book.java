package org.example.bookworm.books;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@NoArgsConstructor
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String publisher;

    @NotNull
    private LocalDate publicationDate;

    @NotBlank
    private String description;

    @NotBlank
    @Size(min = 10, max = 13)
    private String isbn;

    public Book(String title,
                String author,
                String publisher,
                LocalDate publicationDate,
                String description,
                String isbn) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.description = description;
        this.isbn = isbn;
    }

}
