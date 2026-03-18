package org.example.bookworm.books.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBookDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String publisher;

    @NotNull
    @PastOrPresent(message = "Publication date cannot be in the future")
    private LocalDate publicationDate;

    @NotBlank
    private String language;

    @NotBlank
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull
    private Integer pages;

    @NotBlank
    @Size(min = 10, max = 13)
    private String isbn;

}
