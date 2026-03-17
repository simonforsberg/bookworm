package org.example.bookworm.books;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookworm.books.dto.BookDTO;
import org.example.bookworm.books.dto.CreateBookDTO;
import org.example.bookworm.books.dto.UpdateBookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public String listBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @PageableDefault(size = 5) Pageable pageable,
            Model model) {

        Page<BookDTO> page = bookService.search(title, author, pageable);

        model.addAttribute("books", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("title", title);
        model.addAttribute("author", author);

        boolean isSearch = (title != null && !title.isBlank()) || (author != null && !author.isBlank());
        model.addAttribute("isSearch", isSearch);

        return "books/list";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "books/detail";
    }

    @GetMapping("/add")
    public String showCreateBookForm(Model model) {
        model.addAttribute("book", new CreateBookDTO());
        return "books/add";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute("book") CreateBookDTO dto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed when creating book: {}", bindingResult.getAllErrors());
            return "books/add";
        }
        bookService.createBook(dto);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String showUpdateBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "books/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") UpdateBookDTO dto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed when updating book: {}", bindingResult.getAllErrors());
            return "books/edit";
        }
        bookService.updateBook(id, dto);
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

}
