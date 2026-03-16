package org.example.bookworm.books;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(
            ResourceNotFoundException exception,
            Model model) {

        model.addAttribute("message", exception.getMessage());

        return "error/404";
    }

}
