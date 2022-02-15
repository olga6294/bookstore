package com.booksforeveryone.bookstore.application.book.controller.dto;

import lombok.Value;

@Value
public class BookResponseDto {
    int id;
    String title;
    AuthorResponseDto author;
}
