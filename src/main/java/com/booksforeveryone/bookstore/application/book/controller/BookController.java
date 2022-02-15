package com.booksforeveryone.bookstore.application.book.controller;

import com.booksforeveryone.bookstore.application.book.controller.dto.BookResponseDto;
import com.booksforeveryone.bookstore.application.exception.ResourceNotFoundException;
import com.booksforeveryone.bookstore.application.book.service.BookService;
import io.vavr.collection.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/findOne/{id}")
    public BookResponseDto findOne(@PathVariable int id) {
        return bookService.findOne(id).getOrElseThrow(
                () -> new ResourceNotFoundException("Book not found for given id: " + id)
        );
    }

    @GetMapping( "/findAll")
    public List<BookResponseDto> findAll(
            @RequestParam(required = false, defaultValue = "") java.util.List<Integer> bookIds,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "20") int pageSize) {
        return bookService.findAll(List.ofAll(bookIds), page, pageSize);
    }

}
