package org.example.bookworm.books;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        List<BookDTO> books = bookRepository.findAll()
                .stream()
                .map(bookMapper::toDTO)
                .toList();
        log.info("Fetched {} books from database", books.size());
        return books;
    }

    public BookDTO getBookById(Long id) {
        log.info("Fetching book with id {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id {}", id);
                    return new RuntimeException("Book not found: " + id);
                });
        return bookMapper.toDTO(book);
    }

    public BookDTO createBook(CreateBookDTO dto) {
        log.info("Saving new book: {}", dto.getTitle());
        Book book = bookMapper.toEntity(dto);
        BookDTO saved = bookMapper.toDTO(bookRepository.save(book));
        log.info("Book successfully saved with id {}", saved.getId());
        return saved;
    }

    public BookDTO updateBook(Long id, UpdateBookDTO dto) {
        log.info("Updating book with id {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to update: Book not found with id {}", id);
                    return new RuntimeException("Book not found: " + id);
                });
        bookMapper.updateEntity(book, dto);
        BookDTO updated = bookMapper.toDTO(bookRepository.save(book));
        log.info("Book successfully updated with id {}", updated.getId());
        return updated;
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            log.warn("Failed to delete: Non-existent book with id {}", id);
            throw new RuntimeException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Book successfully deleted with id {}", id);
    }

}
