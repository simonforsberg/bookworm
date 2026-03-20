package org.example.bookworm.books;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDTO getBookById(Long id) {
        log.info("Fetching book with id {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with id {}", id);
                    return new ResourceNotFoundException("Book not found with id " + id);
                });
        return bookMapper.toDTO(book);
    }

    @Transactional
    public BookDTO createBook(CreateBookDTO dto) {
        log.info("Create new book: {}", dto.getTitle());
        Book book = bookMapper.toEntity(dto);
        BookDTO saved = bookMapper.toDTO(bookRepository.save(book));
        log.info("Book successfully saved with id {}", saved.getId());
        return saved;
    }

    @Transactional
    public BookDTO updateBook(Long id, UpdateBookDTO dto) {
        log.info("Updating book with id {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to update: Book not found with id {}", id);
                    return new ResourceNotFoundException("Book not found: " + id);
                });
        bookMapper.updateEntity(book, dto);
        BookDTO updated = bookMapper.toDTO(bookRepository.save(book));
        log.info("Book successfully updated with id {}", updated.getId());
        return updated;
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            log.warn("Failed to delete: Non-existent book with id {}", id);
            throw new ResourceNotFoundException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
        log.info("Book successfully deleted with id {}", id);
    }

    public Page<BookDTO> search(String title, String author, Pageable pageable) {
        Page<Book> books;

        if (title != null && author != null) {
            books = bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(title, author, pageable);
        } else if (title != null) {
            books = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (author != null) {
            books = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        } else {
            books = bookRepository.findAll(pageable);
        }

        long totalBooks = bookRepository.count();
        log.info(
                "Search (title='{}', author='{}') → page {} ({} matching elements, {} books total in DB)",
                title,
                author,
                books.getNumber(),
                books.getNumberOfElements(),
                totalBooks
        );

        return books.map(bookMapper::toDTO);
    }

}
