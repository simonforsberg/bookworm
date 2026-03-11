package org.example.bookworm.books;

import lombok.RequiredArgsConstructor;
import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDTO)
                .toList();
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        return bookMapper.toDTO(book);
    }

    public BookDTO createBook(CreateBookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    public BookDTO updateBook(Long id, UpdateBookDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        bookMapper.updateEntity(book, dto);
        return bookMapper.toDTO(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }

}
