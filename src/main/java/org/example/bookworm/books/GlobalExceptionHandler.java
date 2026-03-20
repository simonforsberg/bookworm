package org.example.bookworm.books;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(
            ResourceNotFoundException exception,
            Model model) {
        log.warn("Handled ResourceNotFoundException: {}", exception.getMessage());
        model.addAttribute("message", "The page or resource you're looking for doesn't exist.");

        return "error/404";
    }
}
