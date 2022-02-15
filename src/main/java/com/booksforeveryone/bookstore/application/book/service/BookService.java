package com.booksforeveryone.bookstore.application.book.service;

import com.booksforeveryone.bookstore.application.author.dao.AuthorDao;
import com.booksforeveryone.bookstore.application.book.controller.dto.BookResponseDto;
import com.booksforeveryone.bookstore.application.book.dao.BookDao;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;

    public Option<BookResponseDto> findOne(int id) {
        return bookDao.findOne(id);
    }

    public List<BookResponseDto> findAll(List<Integer> authorIds, int page, int pageSize) {
        if (authorIds.isEmpty()) {
            authorIds = fillAuthorIdsIfNeeded();
        }
        return bookDao.findAll(authorIds, page, pageSize);
    }

    private List<Integer> fillAuthorIdsIfNeeded() {
        return authorDao.findAllAuthorIds();
    }
}
