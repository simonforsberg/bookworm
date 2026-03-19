package org.example.bookworm.books;

import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Book Controller Test")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Nested
    @DisplayName("GET /books")
    class ListBooks {
        @Test
        @DisplayName("GET /books returns list view with books in model")
        void listBooks_shouldReturnListView() throws Exception {
            // Arrange
            PageImpl<BookDTO> page = new PageImpl<>(List.of(new BookDTO()));
            when(bookService.search(isNull(), isNull(), any())).thenReturn(page);

            // Act + Assert
            mockMvc.perform(get("/books"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/list"))
                    .andExpect(model().attribute("books", instanceOf(List.class)))
                    .andExpect(model().attribute("page", instanceOf(Page.class)))
                    .andExpect(model().attribute("isSearch", is(false)));
        }

        @Test
        @DisplayName("GET /books?title=test sets isSearch to true")
        void listBooks_shouldSetIsSearchTrue_whenSearchParamsPresent() throws Exception {
            // Arrange
            when(bookService.search(any(), any(), any()))
                    .thenReturn(Page.empty());

            // Act + Assert
            mockMvc.perform(get("/books")
                            .param("title", "test"))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("isSearch", true));
        }
    }

    @Nested
    @DisplayName("GET /books/{id}")
    class ShowBook {
        @Test
        @DisplayName("GET /books/{id} returns book detail view")
        void showBook_shouldReturnBookDetailView() throws Exception {
            // Arrange
            Long id = 1L;
            BookDTO book = new BookDTO();
            when(bookService.getBookById(id)).thenReturn(book);

            // Act + Assert
            mockMvc.perform(get("/books/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/detail"))
                    .andExpect(model().attribute("book", instanceOf(BookDTO.class)));
        }
    }

    @Nested
    @DisplayName("GET /books/add")
    class ShowAddForm {
        @Test
        @DisplayName("GET /books/add returns add book form view")
        void showCreateBookForm_shouldReturnCreateBookFormView() throws Exception {
            // Act + Assert
            mockMvc.perform(get("/books/add"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/add"))
                    .andExpect(model().attribute("book", instanceOf(CreateBookDTO.class)));
        }
    }

    @Nested
    @DisplayName("POST /books")
    class CreateBook {
        @Test
        @DisplayName("POST /books creates a new book and redirects when input is valid")
        void createBook_shouldRedirect_whenValid() throws Exception {
            // Act + Assert
            mockMvc.perform(post("/books")
                            .param("title", "Test Title")
                            .param("author", "Test Author")
                            .param("publisher", "Test Publisher")
                            .param("publicationDate", "2026-03-19")
                            .param("language", "English")
                            .param("description", "A short description")
                            .param("pages", "200")
                            .param("isbn", "1234567890"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/books"));
            verify(bookService).createBook(any(CreateBookDTO.class));
        }

        @Test
        @DisplayName("POST /books returns add book form view when input is invalid")
        void createBook_shouldReturnCreateBookForm_whenInvalid() throws Exception {
            // Act + Assert
            mockMvc.perform(post("/books")
                            .param("title", ""))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/add"));
            verify(bookService, never()).updateBook(any(), any());
        }
    }

    @Nested
    @DisplayName("GET /books/{id}/edit")
    class ShowEditForm {
        @Test
        @DisplayName("GET /books/{id}/edit returns edit book form view")
        void showUpdateBookForm_shouldReturnUpdateBookFormView() throws Exception {
            // Arrange
            Long id = 1L;
            BookDTO dto = new BookDTO();

            when(bookService.getBookById(id)).thenReturn(dto);

            // Act + Assert
            mockMvc.perform(get("/books/1/edit"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/edit"))
                    .andExpect(model().attribute("book", dto));
            verify(bookService).getBookById(id);
        }
    }

    @Nested
    @DisplayName("POST /books/{id}/edit")
    class UpdateBook {
        @Test
        @DisplayName("POST /books/{id}/edit updates book and redirects when input is valid")
        void updateBook_shouldRedirect_whenValid() throws Exception {
            // Act + Assert
            mockMvc.perform(post("/books/1/edit")
                            .param("title", "Test Title")
                            .param("author", "Test Author")
                            .param("publisher", "Test Publisher")
                            .param("publicationDate", "2026-03-19")
                            .param("language", "English")
                            .param("description", "A short description")
                            .param("pages", "200")
                            .param("isbn", "1234567890"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/books"));
            verify(bookService).updateBook(eq(1L), any(UpdateBookDTO.class));
        }

        @Test
        @DisplayName("POST /books/{id}/edit returns edit form when input is invalid")
        void updateBook_shouldReturnUpdateBookForm_whenInvalid() throws Exception {
            // Act + Assert
            mockMvc.perform(post("/books/1/edit")
                            .param("title", ""))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/edit"));
            verify(bookService, never()).updateBook(any(), any());
        }
    }

    @Nested
    @DisplayName("POST /books/{id}/delete")
    class DeleteBook {
        @Test
        @DisplayName("POST /books/{id}/delete deletes book and redirects")
        void deleteBook_shouldRedirect() throws Exception {
            // Act + Assert
            mockMvc.perform(post("/books/1/delete"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/books"));
            verify(bookService).deleteBook(1L);
        }
    }
}