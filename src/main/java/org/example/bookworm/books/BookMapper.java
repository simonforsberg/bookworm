package org.example.bookworm.books;

import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDTO toDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublicationDate(),
                book.getDescription(),
                book.getIsbn()
        );
    }

    public Book toEntity(CreateBookDTO dto) {
        return new Book(
                dto.getTitle(),
                dto.getAuthor(),
                dto.getPublisher(),
                dto.getPublicationDate(),
                dto.getDescription(),
                dto.getIsbn()
        );
    }

    public void updateEntity(Book book, UpdateBookDTO dto) {
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setPublicationDate(dto.getPublicationDate());
        book.setDescription(dto.getDescription());
        book.setIsbn(dto.getIsbn());
    }

}
