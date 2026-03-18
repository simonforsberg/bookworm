package org.example.bookworm.books;

import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Book Mapper Test")
class BookMapperTest {

    private final BookMapper mapper = new BookMapper();

    @Test
    @DisplayName("Map CreateBookDTO to Book entity")
    void shouldMapCreateDTOtoToEntity() {
        // Arrange
        CreateBookDTO dto = new CreateBookDTO();
        dto.setTitle("Test Title");
        dto.setAuthor("Test Author");

        // Act
        Book book = mapper.toEntity(dto);

        // Assert
        assertNotNull(book);
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
    }

    @Test
    @DisplayName("Map Book entity to BookDTO")
    void shouldMapEntityToDTO() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");

        // Act
        BookDTO dto = mapper.toDTO(book);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Test Title", dto.getTitle());
        assertEquals("Test Author", dto.getAuthor());
    }

    @Test
    @DisplayName("Map UpdateBookDTO to Book entity")
    void shouldMapUpdateBookDTOtoUpdateEntity() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("First title");
        book.setAuthor("First author");

        Long originalId = book.getId();

        UpdateBookDTO dto = new UpdateBookDTO();
        dto.setTitle("Second title");
        dto.setAuthor("Second author");

        // Act
        mapper.updateEntity(book, dto);

        // Assert
        assertEquals(originalId, book.getId());
        assertEquals("Second title", book.getTitle());
        assertEquals("Second author", book.getAuthor());
    }

}