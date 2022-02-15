package com.booksforeveryone.bookstore.application.book.dao;

import com.booksforeveryone.bookstore.application.book.controller.dto.AuthorResponseDto;
import com.booksforeveryone.bookstore.application.book.controller.dto.BookResponseDto;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MergableNamedParameterJdbcTemplate<BookResponseDto> mergableNamedParameterJdbcTemplate;

    public Option<BookResponseDto> findOne(int id) {
        MapSqlParameterSource source = new MapSqlParameterSource();

        source.addValue("id", id);

        String query = "SELECT Book.id, Book.title, Author.firstname, Author.lastname FROM Book "
                     + "INNER JOIN Author ON Book.authorId = Author.id WHERE Book.id = :id";

        BookResponseDto bookResponseDto = namedParameterJdbcTemplate
                .queryForObject(query, source, (resultSet, rowNo) -> new BookResponseDto(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        new AuthorResponseDto(
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname")
                        )
                ));
        return Option.of(bookResponseDto);
    }

    public List<BookResponseDto> findAll(List<Integer> authorIds, int page, int pageSize) {
        MapSqlParameterSource source = new MapSqlParameterSource();

        int offset = (page - 1) * pageSize;

        source.addValue("authorIds", authorIds);

        String query = "SELECT Book.id as id, Book.title as title, "
                     +  "Author.firstname as firstname, Author.lastname as lastname FROM Book "
                     + "INNER JOIN Author ON Book.authorId = Author.id "
                     + "WHERE Author.id IN (:authorIds)";

        java.util.List<BookResponseDto> bookResponseDtoList = mergableNamedParameterJdbcTemplate
                .query(query,
                        "authorIds",
                        authorIds.asJava(),
                        source,
                        offset,
                        pageSize,
                        BookResponseDto::getId,
                        (resultSet, rowNumber) ->
                            new BookResponseDto(
                                    resultSet.getInt("id"),
                                    resultSet.getString("title"),
                                    new AuthorResponseDto(
                                            resultSet.getString("firstname"),
                                            resultSet.getString("lastname")
                                    )
                            )
                );

        return List.ofAll(bookResponseDtoList);
    }
}
