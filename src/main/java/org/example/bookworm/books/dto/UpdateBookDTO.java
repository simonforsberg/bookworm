package org.example.bookworm.books.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private LocalDate publicationDate;

    @NotBlank
    private String language;

    @NotBlank
    private String description;

    @NotNull
    private Integer pages;

    @NotBlank
    @Size(min = 10, max = 13)
    private String isbn;

}
