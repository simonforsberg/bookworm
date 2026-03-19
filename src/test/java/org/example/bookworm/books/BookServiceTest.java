package org.example.bookworm.books;

import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Book Service Test")
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Nested
    @DisplayName("getBookById tests")
    class getBookByIdTests {
        @Test
        @DisplayName("getBookById returns BookDTO when book exists")
        void getBookById_shouldReturnDTO_whenBookExists() {
            // Arrange
            Book book = new Book();
            book.setTitle("Test Title");
            BookDTO dto = new BookDTO();
            dto.setTitle("Test Title");

            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(bookMapper.toDTO(book)).thenReturn(dto);

            // Act
            BookDTO result = bookService.getBookById(1L);

            // Assert
            assertNotNull(result);
            assertEquals("Test Title", result.getTitle());
        }

        @Test
        @DisplayName("getBookById throws ResourceNotFoundException when book doesn't exist")
        void getBookById_shouldThrowException_whenBookNotFound() {
            // Arrange
            when(bookRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));
        }
    }

    @Nested
    @DisplayName("createBook tests")
    class createBookTests {
        @Test
        @DisplayName("createBook returns BookDTO when book is added")
        void createBook_shouldReturnDTO_whenBookIsCreated() {
            // Arrange
            CreateBookDTO createDto = new CreateBookDTO();
            Book book = new Book();

            when(bookMapper.toEntity(createDto)).thenReturn(book);
            when(bookRepository.save(book)).thenReturn(book);
            when(bookMapper.toDTO(book)).thenReturn(new BookDTO());

            // Act
            BookDTO result = bookService.createBook(createDto);

            // Assert
            assertNotNull(result);
            verify(bookMapper).toEntity(createDto);
            verify(bookRepository).save(book);
            verify(bookMapper).toDTO(book);
        }
    }

    @Nested
    @DisplayName("updateBook tests")
    class updateBookTests {
        @Test
        @DisplayName("updateBook returns BookDTO when book is edited")
        void updateBook_shouldReturnDto_whenBookIsUpdated() {
            // Arrange
            UpdateBookDTO updateDto = new UpdateBookDTO();
            Book book = new Book();

            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(bookRepository.save(book)).thenReturn(book);
            when(bookMapper.toDTO(book)).thenReturn(new BookDTO());

            // Act
            BookDTO result = bookService.updateBook(1L, updateDto);

            // Assert
            assertNotNull(result);
            verify(bookMapper).updateEntity(book, updateDto);
            verify(bookRepository).save(book);
            verify(bookMapper).toDTO(book);
        }

        @Test
        @DisplayName("updateBook throws ResourceNotFoundException when book doesn't exist")
        void updateBook_shouldThrowException_whenBookNotFound() {
            // Arrange
            when(bookRepository.findById(1L)).thenReturn(Optional.empty());
            UpdateBookDTO dto = new UpdateBookDTO();

            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, dto));
            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("deleteBook tests")
    class deleteBookTests {
        @Test
        @DisplayName("deleteBook deletes book when book exists")
        void deleteBook_shouldDeleteBook_whenBookExists() {
            // Arrange
            when(bookRepository.existsById(1L)).thenReturn(true);

            // Act
            bookService.deleteBook(1L);

            // Assert
            verify(bookRepository).deleteById(1L);
        }

        @Test
        @DisplayName("deleteBook throws ResourceNotFoundException when book doesn't exist")
        void deleteBook_shouldThrowException_whenBookNotFound() {
            // Arrange
            when(bookRepository.existsById(1L)).thenReturn(false);

            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1L));
            verify(bookRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("search tests")
    class searchTests {
        Pageable pageable = Pageable.unpaged();
        Page<Book> page = Page.empty();

        @Test
        @DisplayName("search returns result when title and author are provided")
        void search_shouldReturnPage_whenTitleAndAuthorExists() {
            // Arrange
            when(bookRepository
                    .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("title", "author", pageable))
                    .thenReturn(page);
            when(bookRepository.count()).thenReturn(0L);

            // Act
            Page<BookDTO> result = bookService.search("title", "author", pageable);

            // Assert
            assertNotNull(result);
            verify(bookRepository)
                    .findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase("title", "author", pageable);
        }

        @Test
        @DisplayName("search returns result when title is provided")
        void search_shouldReturnPage_whenTitleExists() {
            // Arrange
            when(bookRepository
                    .findByTitleContainingIgnoreCase("title", pageable))
                    .thenReturn(page);
            when(bookRepository.count()).thenReturn(0L);

            // Act
            Page<BookDTO> result = bookService.search("title", null, pageable);

            // Assert
            assertNotNull(result);
            verify(bookRepository)
                    .findByTitleContainingIgnoreCase("title", pageable);
        }

        @Test
        @DisplayName("search returns result when author is provided")
        void search_shouldReturnPage_whenAuthorExists() {
            // Arrange
            when(bookRepository.findByAuthorContainingIgnoreCase("author", pageable))
                    .thenReturn(page);
            when(bookRepository.count()).thenReturn(0L);

            // Act
            Page<BookDTO> result = bookService.search(null, "author", pageable);

            // Assert
            assertNotNull(result);
            verify(bookRepository).findByAuthorContainingIgnoreCase("author", pageable);
        }

        @Test
        @DisplayName("search returns all books when called without filters")
        void search_shouldReturnPage_whenSearchIsEmpty() {
            // Arrange
            when(bookRepository.findAll(pageable)).thenReturn(page);
            when(bookRepository.count()).thenReturn(0L);

            // Act
            Page<BookDTO> result = bookService.search(null, null, pageable);

            // Assert
            assertNotNull(result);
            verify(bookRepository).findAll(pageable);
        }
    }
}